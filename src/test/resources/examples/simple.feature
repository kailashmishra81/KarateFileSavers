Feature: JSONPlaceholder API

  Scenario: Get user 1
    * configure proxy = null
    * configure logPrettyRequest = true
    * configure logPrettyResponse = true

    Given url 'https://jsonplaceholder.typicode.com/users/1'
    When method get
    Then status 200
    * print response
    # Save response to a file
    * def jsonFilePath = 'target/json-output/user1.json'
    * karate.write(karate.pretty(response), 'target/json-output/user1.json')

    * print 'Response saved to:', jsonFilePath
