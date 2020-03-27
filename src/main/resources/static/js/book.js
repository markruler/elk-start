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
  // event.submit();
  insertReadingBook.reset();
})

fetch("/all", {
  method: "GET"
}).then(res => {
  return res.json();
}).then(data => {
  console.log(data);
}).catch(err => {
  console.log(err);
});