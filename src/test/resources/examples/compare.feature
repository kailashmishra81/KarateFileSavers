Feature: Compare multiple JSONs using Java

  Scenario: Compare multiple JSON files
    # Load Java classes
    * def JsonComparator = Java.type('com.example.jsoncompare.JsonComparator')
    * def JsonFileHelper = Java.type('com.example.jsoncompare.JsonFileHelper')

    # Folder containing JSONs grouped by scenario
    * def folderPath = 'target/target/json-output/Feature1'
    * def files = JsonFileHelper.getJsonFilesGroupedByScenario(folderPath)

    # Compare scenario-wise
    * def differences = JsonComparator.compareAllScenarios(files)
  #  * print differences

    # Summarize additions/removals scenario-wise
    * def summary = JsonComparator.summarizeAdditionsRemovalsByScenario(files)
   # * print summary


  # Print scenario-wise summary with real newlines
 # convert Java Map keys to array


# iterate over each scenario
 # summary is a Java Map<String, String> but seen as JS object
     # Print scenario-wise summary with proper newlines
    * eval
    """
  for (var scenario in summary) {
  karate.log('==== ' + scenario + ' ====');
  var text = summary[scenario] ? summary[scenario].toString() : '';
  text.split(/\\r?\\n/).forEach(function(line) {
  if (line && line.trim() !== '') {
  karate.log(line);
  }
  });
  }
  """






    # Optionally, call OpenAI or analyze further
    # * eval JsonComparator.analyzeAdditionsRemovals(files, apiKey)
