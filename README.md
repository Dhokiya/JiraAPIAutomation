End to End API Automation Testing of Jira Software: 

cookie-based authentication:-
>>>Logged in to Jira to Create a Session using Login API ( Using SessionFilter Class )
** This SessionFilter class object will store the session details and will be available for further requests.



>>> Created issue using API through RestAssured. 
** Extract value by using JsonPath class and its methods.
** This value will use for further requests and used as a parameter.



>>> Added a comment to an existing Issue using Add Comment API through RestAssured.
** Here in first will define Expected comment which will be parameterized in the body.
** Extracting comment id for verification in further.



>>>Add an attachment to an existing Issue using Add Attachment API. I 
** Here we have used a method called .multipart(), which will responsible for sending the attachment in Jira.



>>Get Issue details and verify if added comment and attachment exists using Get Issue.
** Here will use the comment id for verification.
** After getting the response will the use query parameter to navigate to a particular node(limiting Json response through Query Parameters).
** Using the Assert class ( from TestNG ) will verify the comment.

*** Jira Software (not cloud) is running in localhost. 
*** Links for Jira Server platform REST API reference: https://docs.atlassian.com/software/jira/docs/api/REST/9.3.0/#api/2/issue/{issueIdOrKey}/attachments-addAttachment
