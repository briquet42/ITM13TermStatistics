# ITM13TermStatistics
This is a project for the Practical Software Engineering course of ITM13

## Contribute guidelines
- This repository represents the whole project, not a container for partially solutions. Your implementations should be included in `Java Resources/src`.
- Please do not create unnecessary packages, if there is an existing package which you could use - **USE IT!**
- Take code reviews, submitted code should be reusable, easy to understand and well documented.
- Try your best to produce "clean" code
- Always attach a description to your pull-request, so me or @MICSTI know what's the aim of your pull-request without the need to take a deep look inside your code or commit messages.

## Package Structure
- Root path to java files will be **src/at/fhj/itm/pswe**
- Package **PageCrawler**: Contains all files and the whole functionality of the Pagecrawler
  - Subpackages LinkCrawler, WordAnalyzer according to the different tasks of the algorithm
    - Within every Subpackes contained will be related  Packages such  as Model, Business, Helper, ... 
- Package **Database**: Contains everything with regard to Database Access
  - Examples Would be DAO's, or Connection Classes.
- Package **REST**: Like standard Wildflypackage, contains Endpoints
  - May contain Helper and Business Packages to format data

## "Filename too long" problem under windows
In case you get the "Filename too long" error message while cloning the repository on a windows system there is a git config command to fix it.

- `git config --system core.longpaths true`

## How to work with gulp
In case of editing front-end files (`/WebContent` directory), please take use of **gulp** automation toolkit.
Please do not edit the .min files, those are being generated by gulp.
### What you need to do
- Install **gulp** globally using the node package manager
  - `$ npm install --global gulp`
- Run **gulp** inside the `/WebContent` directory
  - `$ gulp`
  
## Result-File 
- Position: Wildfly-Path/bin/result/crawl
- naming: subdomain_domain_tld_MM_DD_YYYY-HH_MM

Example for `pswengi.bamb.at` started crawling on 23.11.2015 14:21

    pswenig_bamb_at_11_23_2015-14_21.txt
	
Filename can be gathered from `Init_LinkCrawler object` with `.getFilename();`

### Structure
`1 Line:` URL

	http://pswengi.bamb.at
	
`2 Line:` Date and time of crawling the page

	'Sat Nov 23 14:21:53 CET 2015'
	
`3 Line and forward:` Gathered Text from Pages. Each new line is a new Page

##Subsites
- Subsites( Word/Site Overview) are called via Servlet
-url: TermStatistics/SiteOverview/{idOfSite}


## REST-Calls

### Start a Crawler with given URL

	localhost:8080/TermStatistics/rest/action/crawler/http://pswengi.bamb.at