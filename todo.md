/accounts
GET        /accounts -> Employee
GET        /accounts/{iban} -> Customer
- If called by Customer; check if iban in url belongs to userId in JWT.

GET        /accounts/{iban}/transactions -> Customer
- If called by Customer; check if iban in url belongs to userId in JWT.

GET        /accounts/{iban}/balance -> Customer
- If called by Customer; check if iban in url belongs to userId in JWT.

POST        /accounts -> Customer
- Owner of account cannot have isDeleted set to true.
- If called by Customer; check if userId in request body is equal to userId in JWT.
- IBAN, balance, and isDeleted cannot be set, but use generated and/or default values.

PUT        /accounts/{iban} -> Employee
- IBAN, balance, user, and isDeleted are not to be changed.
DELETE    /accounts/{iban} -> Customer
- If called by Customer; check if iban in url belongs to userId in JWT