
# Exchange Rate Service


## Project Overview
It is a simple Sping boot `REST` Application. It exposes some end-points
which can be consumed by front-end application.





## Features

- Retrieve the ECB reference rate for a currency pair, e.g.  USD/EUR  or HUF/EUR.
- Retrieve an exchange rate for other pairs, e.g. HUF/USD.
- Retrieve a list of supported currencies and Count to see how many times they were requested.
- convert an amount in a given currency to another, e.g. 15 EUR = ??? GBP.
- Retrieve a link to a public website showing an interactive chart for a given currency.


## Tech Stack

**Language & Framework:** Java 11 with Spring Boot and Spring-data-jpa

**Build Tool:** Maven

**Server:** Apache Tomcat

**Database:** H2


## Run Locally

Download the project

```bash
  git clone https://github.com/TrRavi/exchangerateservice.git
```

Go to the project directory

```bash
  cd exchangerateservice
```

Fetch dependencies and build jar using maven

```bash
  mvn clean install
```

Navigate to `project directory/traget` and start the server( running the jar)

```bash
  java -jar exchangerateservice.jar
```


## API List (Examples)

##### For getting reference rate between given currency pair
```
GET :: http://localhost:8080/api/referencerate?baseCurrency=INR&toCurrency=CAD
```
```JSON
{
    "baseCurrency": "CAD",
    "currency": "INR",
    "exchangeRate": 58.881,
    "date": "2022-02-24"
}
```

##### For getting the list of supported currencies and uses count (how many times they were requested?)
```
GET :: http://localhost:8080/api/supportedCurrency
```
```JSON
{
    "CHF": 4,
    "HRK": 2,
    "MXN": 0,
    "ZAR": 2,
    "INR": 2,
    "CNY": 1
    //...
}
```
##### for Converting an amount in a given currency to another, e.g. 15 EUR = ??? GBP
```
GET :: http://localhost:8080/api/convertCurrency?baseCurrency=EUR&toCurrency=GBP&baseCurrencyAmount=10
```
```JSON
{
    "baseCurrency": "EUR",
    "toCurrency": "GBP",
    "baseCurrencyAmount": 10.0,
    "toCurrencyAmount": 8.374
}
```

```
#### Retrieve link of public website showing an interactive chart for a given currency. (Base Currency:=> EUR)
```
GET :: http://localhost:8080/api/chart?currency=INR
```
```JSON
{
    "baseCurrency": "EUR",
    "currency": "inr",
    "chartURL": [
        "http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-inr.en.html?date=2022-02-25&rate=84.3470",
        "http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-inr.en.html?date=2022-02-24&rate=84.2960",
        "http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-inr.en.html?date=2022-02-23&rate=84.6135",
        "http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-inr.en.html?date=2022-02-22&rate=84.7580",
        "http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-inr.en.html?date=2022-02-21&rate=84.6770"
    ]
}
```

## Postman Collection 
 - [Postman Collection](https://drive.google.com/file/d/11_tvbcEFnWYZ0SNdWSIQBp9a6bMcfxkF/view?usp=sharing)

## Author

- [Ravi kumar](https://www.linkedin.com/in/7284ravi/)

