// https://echarts.apache.org/examples/en/editor.html?c=pie-legend
let pieOption = {
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

const pieChart = echarts.init(
  document.getElementsByClassName("pie-chart")[0],
  "default",
  {
    renderer: "canvas"
  }
);

pieChart.setOption(pieOption);

fetch("/pie", {
  method: "GET"
})
.then(res => {
  console.log(res);
  return res.json();
})
.then(data => {
  // console.log(data);
  pie_chart(pieChart, data, pieOption);
  pieChart.resize();
})
.catch(err => {
  console.log(err);
});

let testPie;
let testNames;
let testValues;
// const chartColor = pieChart["_theme"]["color"];
function pie_chart(chart, data, pieOption) {
  console.log(data);
  // chart.clear();
  testPie = data;

  //차트 옵션 설정
  const names = Object.keys(data);
  testNames = names;
  const values = Object.values(data);
  testValues = values;

  for (let i = 0; i < Object.keys(values[0]).length; i++) {
    pieOption.series[0].data.push({
      name: Object.keys(values[0])[i],
      value: Object.values(values[0])[i]
    })
  }
  pieOption.title.text = `${names[0]} 분야별 독서 비중`;
  // Object.values(data["2019-11-01"]);

  chart.setOption(pieOption);
}

//차트 resize
window.onresize = () => {
  if (pieChart != null && pieChart != undefined) {
    pieChart.resize();
  }
};