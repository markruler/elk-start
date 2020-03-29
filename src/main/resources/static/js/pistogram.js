// https://echarts.apache.org/examples/en/editor.html?c=dataset-link
let pistogramOption = {
  title: {
    text: ""
  },
  series: [{
    name: "독서량",
    data: [],
    type: 'pie',
    radius: "55%",
    center: ["40%", "50%"],
  }],
  tooltip: {
    trigger: "item",
    formatter: "{a} <br/> {b} : {c}권"
  },
  legend: {
    type: "scroll",
    orient: "vertical",
    right: "10",
  }
};

const pistogramChart = echarts.init(
  document.getElementsByClassName("pistogram-chart")[0],
  "default",
  {
    renderer: "canvas"
  }
);

pistogramChart.setOption(pistogramOption);

fetch("/pie", {
  method: "GET"
})
.then(res => {
  console.log(res);
  return res.json();
})
.then(data => {
  // console.log(data);
  pistogram_chart(pistogramChart, data, pistogramOption);
  pistogramChart.resize();
})
.catch(err => {
  console.log(err);
});

let testPie;
let testNames;
let testValues;
// const chartColor = pistogramChart["_theme"]["color"];
function pistogram_chart(chart, data, pistogramOption) {
  console.log(data);
  // chart.clear();
  testPie = data;

  //차트 옵션 설정
  const names = Object.keys(data);
  testNames = names;
  const values = Object.values(data);
  testValues = values;

  for (let i = 0; i < Object.keys(values[0]).length; i++) {
    pistogramOption.series[0].data.push({
      name: Object.keys(values[0])[i],
      value: Object.values(values[0])[i]
    })
  }
  pistogramOption.title.text = `월간`;
  // Object.values(data["2019-11-01"]);

  chart.setOption(pistogramOption);
  make_result(names, values);
}

function make_result(names, values) {
  const result = document.getElementsByClassName('kwd-result')[0];
  let resultHtml;
  result.innerHTML = "";
  console.log("names : \n", names);
  console.log("values : \n", values);
  for (let i = 0; i < names.length; i++) {
    resultHtml += `<ul class="con-ul">
                    <li class="no-li">
                      <div>
                        <span>${names[i]}</span>
                      </div>
                    </li>
                    <li class="kwd-li">
                      ${values[i]}건
                    </li>
                  </ul>`;
  }
  result.innerHTML = resultHtml;
}

//차트 resize
window.onresize = () => {
  if (pistogramChart != null && pistogramChart != undefined) {
    pistogramChart.resize();
  }
};