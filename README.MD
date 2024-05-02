# About

A SpringBootRest project that has enpoints described in [file](spring_boot_rest-openapi.yaml)

## Example Input JSON

[Json File](/src/main/resources/populatePlayers.json) to populate players
##
### Endpoints

| Method   | URL                  | Description                                                                                |
|----------|----------------------|--------------------------------------------------------------------------------------------|
| `GET`    | `/api/team`          | Retrieve all teams                                                                         |
| `POST`   | `/api/team`          | Create a new team. [Example of body](#create-team)                                         |
| `PUT`    | `/api/team/{id}`     | Update team by id path parameter. [Example of body](#update-team)                          |
| `DELETE` | `/api/team/{id}`     | Delete team by id path parameter.                                                          |
| `POST`   | `/api/player`        | Create a new player. [Example of body](#create-player)                                     |
| `GET`    | `/api/player/{id}`   | Get player by id path parameter                                                            |
| `PUT `   | `/api/player/{id}`   | Update player by id path parameter                                                         |
| `DELETE` | `/api/player/{id}`   | Delete player by id path parameter                                                         |
| `POST`   | `/api/player/_list`  | Get teams with specified fields, page and size [Example of body](#list-players)            |
| `POST`   | `/api/player/report` | Get players with specified field in .csv file  [Example of body](#report-players)          |
| `POST`   | `/api/player/upload` | Upload players from json file  [Example of json](/src/main/resources/populatePlayers.json) |

####


## Create Team

```json
{
  "name": "string"
}
```

## Update Team

```json
{
  "name": "string"
}
```

## Create Player

```json
{
  "name": "Jayson",
  "surname": "Tatum",
  "year_of_birth": 1998,
  "position": "shooter, power forward",
  "teamId": 1
}
```
## List Players

```json
{
  "name": "string",
  "surname": "string",
  "page": 0,
  "size": 0
}
```
## Report Players

```json
{
  "name": "string",
  "surname": "string"
}
```
