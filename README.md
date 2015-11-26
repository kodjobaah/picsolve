# picsolve

## To use this app:

### Database Configuration
You will need to modifiy the application.conf so that it points to your mysql instance.

### ToDoItem entry creation
To create an entry you will need to post a json string to the appropriate end point.

Endpoint: http://localhost:8080/todolist
postdata: {"id":-1,"priority":1,"description": "this is what i am doing", "isDone":false} 

This will return the json data with the id updated to reflect the items' location within the database

### Get list of all todo items
To get a list of all items you will need to make a http get request to the end point below:

endpoint:http://localhost:8080/todolist/list

This will return a json string containing a list of all items in the database.

e.g

[4]
0:  {
id: 1
priority: 1
description: "hello kodjo"
isDone: false
}-
1:  {
id: 2
priority: 1
description: "this is what i am doing"
isDone: false
}-
2:  {
id: 3
priority: 1
description: "hello kodjo"
isDone: false
}-
3:  {
id: 4
priority: 2
description: "hello kodjo"
isDone: false
}

