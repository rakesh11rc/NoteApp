# NoteApp Instructions

## Running the app: go into the project folder and execute the below command
docker-compose up

##Endpoints

###NoteBook

#### Get all notebooks (GET)
* http://localhost:8080/v1/notebook

#### Get a notebook (GET)
* http://localhost:8080/v1/notebook/{id}

#### Get a notebook with tags filter (GET)
##### Example: http://localhost:8080/v1/notebook/abcde/tag1,tag2
* http://localhost:8080/v1/notebook/{id}/tags

#### Create notebook (POST)
##### Request body example:
##### {"title": "test1"}
* http://localhost:8080/v1/notebook

#### Update notebook (PUT)
##### Request body example:
##### {ïd": "xxxx", title": "newTitle", "notes": \[{..},{..}]}
* http://localhost:8080/v1/notebook

#### Delete a notebook (DELETE)
* http://localhost:8080/v1/notebook/{id}

###Note
#### Get note (GET)
* http://localhost:8080/v1/notebook/{id}/note/{noteId}

#### Create a note (POST)
##### Request body example:
##### 
{
"title": "xxxx",
"tags": ["xxxx",
        "xxxx"]
}
* http://localhost:8080/v1/notebook/{id}/note

#### Update a note (PUT)
##### Request body example:
##### 
{
"id": "xxxx",
"title": "xxxx",
"createdDate": "xxxx",
"lastModifiedDate": "xxxx",
"tags": ["xxxx",
"xxxx"]
}
* http://localhost:8080/v1/notebook/{id}/note


#### Delete note (DELETE)
* http://localhost:8080/v1/notebook/{id}/note/{noteId}

