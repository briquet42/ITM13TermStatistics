package at.fhj.itm.pswe.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.json.JSONArray;
import org.json.JSONObject;

import at.fhj.itm.pswe.dao.interfaces.IWord;
import at.fhj.itm.pswe.model.Word;

@Stateless
public class WordDao implements IWord {


	private EntityManager em;

	@PersistenceContext(unitName = "TermStatistics")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}


	@Override
	public JSONArray wordAndAmount() {

		List<Object[]> queryResult = em
				.createQuery(
						"SELECT w.text, w.active, sum(c.amount), w.wordType.id, w.wordType.texttype  FROM Container c JOIN c.word w  GROUP BY w.text, w.active")
						.getResultList();

		JSONArray result = new JSONArray();

		for (Object[] wo : queryResult) {
			JSONObject word = new JSONObject();
			word.put("word", wo[0]);
			word.put("amount", wo[2]);
			word.put("active", wo[1]);
			word.put("wType", wo[3]);
			
			JSONObject wordType = new JSONObject();
			wordType.put("name", wo[4]);
			
			JSONObject complete = new JSONObject();
			complete.put("word", word);
			complete.put("wTypes", wordType);

			result.put(complete);
		}

		return result;
	}
	
	@Override
	public JSONArray wordTypeAsOption(){
		List<Object[]> queryResult = em
				.createQuery(
						"SELECT wt.id, wt.texttype  FROM WordType wt  GROUP BY w.text, w.active")
						.getResultList();

		JSONArray result = new JSONArray();

		for (Object[] wo : queryResult) {
			JSONObject wordType = new JSONObject();
			wordType.put("label", wo[1]);
			wordType.put("value", wo[0]);
			
			result.put(wordType);
		}

		return result;
	}
	
	

	@Override
	public JSONArray sitesOfWord(String word) {
		Query q = em.createQuery("SELECT c.website.id, c.website.domain, sum(c.amount) "
				+ "FROM Container c WHERE c.word.text LIKE :word AND c.word.active = TRUE "
				+ "GROUP BY c.website ORDER BY sum(c.amount) DESC").setParameter("word", word);

		List<Object[]> queryResults = q.getResultList();

		JSONArray result = new JSONArray();

		for (Object[] wo : queryResults) {
			//System.out.println("sitesOfWord: " + wo[0] + " | " + wo[1]);

			JSONObject temp = new JSONObject();
			temp.put("id", wo[0]);
			temp.put("adresse", wo[1]);
			temp.put("amount", wo[2]);

			result.put(temp);
		}

		return result;

	}

	@Override
	public void changeWordActive(String word, boolean active) {
		Word wo = em.find(Word.class, word);
		if(wo != null){
			wo.setActive(active);
			//save changes to db
			em.flush();
		}
		//do nothing if no word is found


	}

	@Override
	public JSONObject findSingleWordWithAmount(String word) {
		List<Object[]> queryResult = em
				.createQuery(
						"SELECT w.text, w.active, sum(c.amount)  FROM Container c JOIN c.word w WHERE w.text = :word  GROUP BY w.text, w.active")
						.setParameter("word", word).getResultList();
		JSONObject result = new JSONObject();

		if (!queryResult.isEmpty()) {
			result.put("word", queryResult.get(0)[0]);
			result.put("amount", queryResult.get(0)[2]);
			result.put("active", queryResult.get(0)[1]);
		}
		return result;
	}

	@Override
	public JSONArray wordCountOverPeriod(String word, String startdate, String enddate) {
		Query countWordPeriod = em.createQuery("SELECT co.logDate, SUM(co.amount) FROM Container co "
				+ "WHERE co.word.text = :word AND (co.logDate BETWEEN :startdate AND :enddate) "
				+ "GROUP BY co.logDate");
		countWordPeriod.setParameter("word", word);
		countWordPeriod.setParameter("startdate", startdate);
		countWordPeriod.setParameter("enddate", enddate);

		final List<Object[]> results = countWordPeriod.getResultList();

		JSONArray result = new JSONArray();

		for (Object[] wo : results) {
			System.out.println("wordCountOverPeriod: " + wo[0] + " | " + wo[1]);

			JSONObject temp = new JSONObject();
			temp.put("date", wo[0]);
			temp.put("amount", wo[1]);

			result.put(temp);
		}

		return result;
	}

}
