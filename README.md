# GameRentApp

## Overview
This is java backend using spring boot for app to game rent mamagenet including user quota management

The app contains DAO for various tables. 

1. UserDAO (This Dao is for USERS table, which contains primarily username and name, can be extended further to more use cases)
2. GameDAO (This Dao if for GAMES table, which contain game details(title and studio))
3. GenreDAO (This Dao if for GENRES table, which contain genre details(genreName and genreDetail))
4. GenreGameRelationDAO (This table is used to store m:n relation between game and genre)
5. UserGameRelationDAO (This table is used to store m:n relation between user and game)

The app contains following main controller classes

1. UserConrtoller: This controller have rest APIs for managing users DB
2. GenreController: This controller have rest APIs for managing genres DB
3. GameController: This controller have rest APIs for managing games DB
4. GameRentController: This controller handle main interaction of app between user and games, ie renting games or listing all the games rented.

## Important behaviour of App
* For all the rest APIs, have added basic vallidation
* Throwing proper exception, as and when required (there is scope of creating cutom exception wrapper)
* If New Game is created with genre which are not present, New entry for genres is created with few default values
* Some modification in Username (for USERS table) and genreName (for GENRES table) have been done before storing in database, To avoid duplicating entries
* If new game rent request Is received for a user, we have proper validation in place
 *   To check if user exist
 *   To check if game exist
 *   Have user already exhasuted the quota
*   When all remnted games list is requested by user, we send complete response with games and genre details as well (not just IDs)

## PS
Please use Postman-Collection added for more detailed description of APIs and easy local testing.

