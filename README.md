# AndroidJSONForms
This app will create dynamic forms based on the provided input JSON (in a specific format)

###Input types supported as of now:
1. Text
2. Number
3. Multiline
4. Dropdown

###Validations supported:
1. Required - Can be used for types text, number and multiline
2. min, max -  Can be used for number types only.

For usages see the JSON format below.

### Input JSON
You can make changes to input json in this file:`/app/src/main/assets/formDetails.json`
```json
[
  {"field-name":"name", "type":"text", "required": true},
  {"field-name":"age", "type":"number", "min": 18, "max": 60},
  {"field-name":"address", "type":"multiline"},
  {"field-name":"gender", "type":"dropdown", "options":["male", "female", "other"]}
]

```