# CognitoDemoThymeleaf
This is the CognitoDemo application with web pages rewritten to use Thymeleaf templates, rather than the Java Server Pages used by the CognitoDemo application.

The Thymeleaf templating engine (see https://www.thymeleaf.org/) is popular in the Spring framework community. Often Spring examples use Thymeleaf for the web page templates, instead of Java Server Pages (JSP). One example of this is Spring reference *Spring in Action*, Fifth Edition, by Craig Walls from Manning Press.

I have been working on the architecture for a large scale web application (I previously designed and built the nderground social network - see nderground.net). JSP is an old technology and it is not clear that Oracle is doing much to maintain, much less extend JSP. So I have been looking at other server side web page rendering technologies (i.e., template engines). I have been focusing on the Spring framework, so Thymeleaf was an obvious technology to explore.

To gain experience with Thymeleaf I rewrote the web pages for the CognitoDemo application ( https://github.com/IanLKaplan/CognitoDemo ) using Thymeleaf tags. In addition, I added an AppConfig class (see cognitodemo.thymeleaf.config.AppConfig) which adds the Spring configuration needed by Thymeleaf. Anyone new to Thymeleaf will probably find this class a useful reference.

One of the Thymeleaf features that I found attractive is the ability to include web page fragements in a web page. I used fragements to include the standard web page declarations for Bootstrap and other packages. This makes web pages easier to develop and limits changes to the web page header to a single file. I also added a page header fragement that is included in every page.

A reader familiar with JSP can compare the JSP pages in (CognitoDemo)[https://github.com/IanLKaplan/CognitoDemo] to the pages in this repository to see how various JSP features are implemented in Thymeleaf.

One problem I had with Thymeleaf is the poor error messages. Or, more to the point, nonexistent error messages. For example, in the JSP code in the change_password.jsp page there is the following conditional. Note that in the JSP I used single quotes to surround the conditional.

```html
		  <c:when test='${change_type != null && change_type.equals("change_password")}'>
		       <input class="invisible" type="text" id="user_name" name="user_name" value="${user_name_val}">
		  </c:when>
```

When I changed this JSP to Thymeleaf (in the file change_password.html) I used double quotes and left the quoted string "change_password" in double quotes.

This caused problems with the Thymeleaf parser. Instead of attempting to provide an error message with at least a line number, the Thymeleaf parser just throws a Java error with no indication of where the error occurred.

I was able to track the problem down and change the conditional construct as shown below.

```html
	    <div th:if="${change_type != null && change_type.equals('change_password')}">
	         <input class="invisible" type="text" id="user_name" name="user_name" th:value="${user_name_val}">
	    </div>
```

Thymeleaf errors can be difficult to track down and I am concerned that this will add to the development time for a large web application.

Although Thymeleaf is popular in the Spring community and is a template package that is supported by the Spring Tool Suite project builder, I noticed that Thymeleaf is not supported by Pivotal (the company that supports the Spring framework). Thymeleaf is supported three GitHub committers and most of the commits are from two people. If these GitHub committers are not able or willing to support Thymeleaf, the project could become an orphan. Since I am designing the large web application, this could impose a significant future cost.

In contrast the FreeMarker template engine is an Apache project (https://freemarker.apache.org/) with the Apache community behind it. The same is true of the Velocity template engine (http://velocity.apache.org/), although Velocity is not officially supported by Spring.

The communities for FreeMarker and Velocity appear to be large, which reduces the likelihood that these template engines will become orphans. This may make these frameworks a safer invenstment than Thymeleaf.
