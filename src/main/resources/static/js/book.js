const insertReadingBook = document.getElementById("insertReadingBook");
insertReadingBook.addEventListener("submit", (event) => {
  event.preventDefault();
  const formData = new FormData(insertReadingBook);
  let jsonData = {};
  for (const [key, value] of formData.entries()) {
    console.log(`${key} : ${value}`);
    jsonData[key] = value;
  }
  console.log(JSON.stringify(jsonData));
  fetch("/insert", {
    method: "POST",
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(jsonData)
  }).then(res => {
    console.log(res);
  }).catch(err => {
    console.log(err);
  });
  insertReadingBook.reset();
})

const nested = {
  "size": 0,
  "aggs": {
    "group_by_state": {
      "date_range": {
        "field": "createDateTime",
        "ranges": [
          {
            "from": "now-2M/M",
            "to": "now-1M/M"
          }
        ]
      },
      "aggs": {
        "category_count": {
          "terms": {
            "field": "category"
          }
        }
      }
    }
  }
}
// console.log(JSON.parse(JSON.stringify(jsonData)));

// fetch("/agg", {
//   method: "POST",
//   headers: {
//     "Content-Type": "application/json"
//   },
//   body: JSON.stringify(jsonData)
  
// }).then(res => {
//   console.log(res);
//   return res.json();
// }).then(data => {
//   console.log(data);
// }).catch(err => {
//   console.log(err);
// });

// fetch("http://localhost:9200/reading_books/_search", {
//   method: "POST",
//   headers: {
//     "Access-Control-Allow-Origin": "*",
//     "Access-Control-Allow-Methods": "GET, POST, OPTIONS",
//     // "credentials": "same-origin",
//     "mode": "no-cors",
//     "Content-Type": "application/json"
//   },
//   body: JSON.stringify(jsonData)
// }).then(res => {
//   console.log(res);
// }).catch(err => {
//   console.log(err);
// });