# webfeed
Check some webpages, search for new links etc, send mail about the news

Suggested usage from cron: pipe output to mail command

	0 * * * * sebcsaba java -jar webfeed-0.0.1-jar-with-dependencies.jar config.mytask.properties | /bin/mail -s "mytask feed" mymail@mail.com
