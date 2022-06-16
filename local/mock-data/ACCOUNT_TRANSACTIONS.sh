# shellcheck disable=SC2016

curl -X POST \
  http://0.0.0.0:8082/topics/ACCOUNT_TRANSACTIONS \
  -H 'accept: application/vnd.kafka.v2+json' \
  -H 'content-type: application/vnd.kafka.json.v2+json' \
  --silent --output /dev/null \
  -d '{
    "records": [
      {
        "key" : "3c04fcce-65fa-4115-9441-2781f6706ca7",
        "value": {
          "uniqueIdentifier": "3c04fcce-65fa-4115-9441-2781f6706ca7",
          "amount": "85",
          "currency": "CHF",
          "ibanAccount": "CH93-0000-0000-0000-0000-0",
          "valueDate": "08-07-2022",
          "description": "Online payment CHF"
        }
      }, {
        "key": "cddfd78d-5ace-4b52-be73-aac571397f7a",
        "value": {
          "uniqueIdentifier": "cddfd78d-5ace-4b52-be73-aac571397f7a",
          "amount": "100",
          "currency": "GBP",
          "ibanAccount": "CH93-0000-0000-0000-0000-0",
          "valueDate": "07-07-2022",
          "description": "Online payment GBP"
        }
      },
       {
        "key" : "d689043c-71fc-4e43-bd74-5ccfa6b07c94",
        "value": {
          "uniqueIdentifier": "d689043c-71fc-4e43-bd74-5ccfa6b07c94",
          "amount": "85",
          "currency": "CHF",
          "ibanAccount": "CH93-0000-0000-0000-0000-1",
          "valueDate": "06-07-2022",
          "description": "Online payment CHF"
        }
      }, {
        "key": "4d2eb11a-0285-4ffe-aa72-53475ee4360e",
        "value": {
          "uniqueIdentifier": "4d2eb11a-0285-4ffe-aa72-53475ee4360e",
          "amount": "100",
          "currency": "GBP",
          "ibanAccount": "CH93-0000-0000-0000-0000-1",
          "valueDate": "06-07-2022",
          "description": "Online payment GBP"
        }
      },
       {
        "key" : "f4a45a6b-dcf9-45b3-ba82-24296d89e3fe",
        "value": {
          "uniqueIdentifier": "f4a45a6b-dcf9-45b3-ba82-24296d89e3fe",
          "amount": "85",
          "currency": "CHF",
          "ibanAccount": "CH93-0000-0000-0000-0000-1",
          "valueDate": "05-07-2022",
          "description": "Online payment CHF"
        }
      }, {
        "key": "f4d0b259-169a-479b-b6da-0727d09e2147",
        "value": {
          "uniqueIdentifier": "f4d0b259-169a-479b-b6da-0727d09e2147",
          "amount": "100",
          "currency": "GBP",
          "ibanAccount": "CH93-0000-0000-0000-0000-1",
          "valueDate": "04-07-2022",
          "description": "Online payment GBP"
        }
      },
       {
        "key" : "2d877176-e91c-4a17-b1b2-00b10c81b269",
        "value": {
          "uniqueIdentifier": "2d877176-e91c-4a17-b1b2-00b10c81b269",
          "amount": "85",
          "currency": "CHF",
          "ibanAccount": "CH93-0000-0000-0000-0000-1",
          "valueDate": "03-07-2022",
          "description": "Online payment CHF"
        }
      }, {
        "key": "92428596-1b9c-43d1-8612-42f9025365d4",
        "value": {
          "uniqueIdentifier": "92428596-1b9c-43d1-8612-42f9025365d4",
          "amount": "100",
          "currency": "GBP",
          "ibanAccount": "CH93-0000-0000-0000-0000-1",
          "valueDate": "02-07-2022",
          "description": "Online payment GBP"
        }
      }, {
        "key" : "219f2ce9-5444-43f3-bea4-dcc3ae17244a",
        "value": {
          "uniqueIdentifier": "219f2ce9-5444-43f3-bea4-dcc3ae17244a",
          "amount": "85",
          "currency": "CHF",
          "ibanAccount": "CH93-0000-0000-0000-0000-1",
          "valueDate": "01-07-2022",
          "description": "Online payment CHF"
        }
      }, {
        "key": "c6d8c362-251c-4da2-8e69-77bb494f0b11",
        "value": {
          "uniqueIdentifier": "c6d8c362-251c-4da2-8e69-77bb494f0b11",
          "amount": "100",
          "currency": "GBP",
          "ibanAccount": "CH93-0000-0000-0000-0000-1",
          "valueDate": "05-07-2022",
          "description": "Online payment GBP"
        }
      }, {
        "key" : "8caa83e4-ba93-4aa6-9124-2e73b3be688b",
        "value": {
          "uniqueIdentifier": "8caa83e4-ba93-4aa6-9124-2e73b3be688b",
          "amount": "85",
          "currency": "CHF",
          "ibanAccount": "CH93-0000-0000-0000-0000-1",
          "valueDate": "10-07-2022",
          "description": "Online payment CHF"
        }
      }, {
        "key": "a94a8a18-f90d-4892-a0e3-afd1c300b606",
        "value": {
          "uniqueIdentifier": "a94a8a18-f90d-4892-a0e3-afd1c300b606",
          "amount": "100",
          "currency": "GBP",
          "ibanAccount": "CH93-0000-0000-0000-0000-1",
          "valueDate": "15-07-2022",
          "description": "Online payment GBP"
        }
      }, {
        "key" : "0253d251-e875-436d-a5a0-e23e359cdb35",
        "value": {
          "uniqueIdentifier": "0253d251-e875-436d-a5a0-e23e359cdb35",
          "amount": "85",
          "currency": "CHF",
          "ibanAccount": "CH93-0000-0000-0000-0000-1",
          "valueDate": "20-07-2022",
          "description": "Online payment CHF"
        }
      }, {
        "key": "0d3731b6-e905-45a3-b31e-8938597cacd2",
        "value": {
          "uniqueIdentifier": "0d3731b6-e905-45a3-b31e-8938597cacd2",
          "amount": "100",
          "currency": "GBP",
          "ibanAccount": "CH93-0000-0000-0000-0000-1",
          "valueDate": "25-07-2022",
          "description": "Online payment GBP"
        }
      }, {
        "key" : "4b19f7b6-99e5-455d-a6d0-48fbe269cdaf",
        "value": {
          "uniqueIdentifier": "4b19f7b6-99e5-455d-a6d0-48fbe269cdaf",
          "amount": "85",
          "currency": "CHF",
          "ibanAccount": "CH93-0000-0000-0000-0000-1",
          "valueDate": "30-07-2022",
          "description": "Online payment CHF"
        }
      }, {
        "key": "8531afa6-f535-4026-8771-28b110ab5398",
        "value": {
          "uniqueIdentifier": "8531afa6-f535-4026-8771-28b110ab5398",
          "amount": "100",
          "currency": "GBP",
          "ibanAccount": "CH93-0000-0000-0000-0000-1",
          "valueDate": "01-06-2022",
          "description": "Online payment GBP"
        }
      }, {
        "key" : "6c17e756-41da-482d-b9ce-e1097519263b",
        "value": {
          "uniqueIdentifier": "6c17e756-41da-482d-b9ce-e1097519263b",
          "amount": "85",
          "currency": "CHF",
          "ibanAccount": "CH93-0000-0000-0000-0000-1",
          "valueDate": "8-06-2022",
          "description": "Online payment CHF"
        }
      }, {
        "key": "e956614d-feff-4d56-99fd-52f2f7d3d351",
        "value": {
          "uniqueIdentifier": "e956614d-feff-4d56-99fd-52f2f7d3d351",
          "amount": "100",
          "currency": "GBP",
          "ibanAccount": "CH93-0000-0000-0000-0000-1",
          "valueDate": "15-06-2022",
          "description": "Online payment GBP"
        }
      }, {
        "key" : "2b415f80-7c4d-448e-924f-5c98a9ded73b",
        "value": {
          "uniqueIdentifier": "2b415f80-7c4d-448e-924f-5c98a9ded73b",
          "amount": "85",
          "currency": "CHF",
          "ibanAccount": "CH93-0000-0000-0000-0000-1",
          "valueDate": "23-06-2022",
          "description": "Online payment CHF"
        }
      }, {
        "key": "dacb90ff-2f6a-4f22-a2af-81de4a36087a",
        "value": {
          "uniqueIdentifier": "dacb90ff-2f6a-4f22-a2af-81de4a36087a",
          "amount": "100",
          "currency": "EUR",
          "ibanAccount": "CH93-0000-0000-0000-0000-1",
          "valueDate": "30-06-2022",
          "description": "Online payment EUR"
        }
      }
    ]
  }'