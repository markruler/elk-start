let option = {
  title: {
    text: "월간 독서량"
  },
  // responsive: true,
  xAxis: {
    type: 'category',
    data: [],
    // axisPointer: {
    //   snap: true,
    //   lineStyle: {
    //     color: '#004E52',
    //     opacity: 0.5,
    //     width: 2
    //   },
    //   label: {
    //     show: true,
    //     formatter: function (params) {
    //       return echarts.format.formatTime('yyyy-MM-dd', params.value);
    //     },
    //     backgroundColor: '#004E52'
    //   },
    //   handle: {
    //     show: true,
    //     color: '#004E52'
    //   }
    // }
  },
  yAxis: {
    type: 'value',
    axisLabel: {
      formatter: "{value} 권"
    },
    axisPointer: {
      snap: true
    }
  },
  tooltip: {
    trigger: 'axis',
    formatter: "{a} <br/> {b} : {c}권",
    axisPointer: {
      type: 'cross',
      label: {
        backgroundColor: '#6a7985'
      }
    }
  },
  toolbox: {
    show: true,
    left: "center",
    itemSize: 20,
    feature: {
      dataZoom: {
        yAxisIndex: 'none',
        title: {
          zoom: "zoom",
          back: "back"
        }
      },
      // dataView: {
      //   title: "data view",
      //   readOnly: true
      // },
      magicType: {
        type: ["line", "bar"],
        title: {
          line: "line",
          bar: "bar"
        }
      },
      restore: {
        title: "restore"
      },
      saveAsImage: {
        title: "save"
      }
    }
  },
  grid: {
    // top: "5%",
    // bottom: "25%",
    right: "20%"
  },
  legend: {
    right: "0",
    bottom: "50%"
  },
  dataZoom: [{
      startValue: '2019-11-01'
  }, {
      type: 'inside'
  }],
  visualMap: {
    // top: 50,
    right: 10,
    pieces: [{
      gt: 0,
      lte: 20,
      color: '#096'
    }, {
      gt: 20,
      lte: 40,
      color: '#ffde33'
    }, {
      gt: 40,
      lte: 60,
      color: '#ff9933'
    }, {
      gt: 60,
      lte: 80,
      color: '#cc0033'
    }, {
      gt: 80,
      lte: 100,
      color: '#660099'
    }, {
      gt: 100,
      color: '#7e0023'
    }],
    outOfRange: {
      color: '#999'
    }
  },
  series: [{
    name: "독서량",
    data: [],
    type: 'line',
    smooth: false,
    label: {
      formatter: "{d}권"
    },
    markPoint: {
      data: [
        {type: 'max', name: '최댓값'},
        {type: 'min', name: '최솟값'}
      ]
    },
    markLine: {
      silent: true,
      data: [
        {type: 'average', name: '평균값'},
        [{
          symbol: 'arrow',
          x: '80%',
          yAxis: 'max'
        }, {
          symbol: 'diamond',
          label: {
            position: 'start',
            formatter: '최대값'
          },
          type: 'max',
          name: '최대값'
        }],
        [{
          symbol: 'arrow',
          x: '80%',
          yAxis: 'min'
        }, {
          symbol: 'rect',
          label: {
            position: 'start',
            formatter: '최솟값'
          },
          type: 'min',
          name: '최솟값'
        }]
      ]
    },
    markArea: {
      data: [
        [{
          name: '연말연초',
          xAxis: '2019-12-01'
        }, {
          xAxis: '2020-01-01'
        }]
      ]
    }
  }]
};

const lineChart = echarts.init(
  document.getElementsByClassName("line-chart")[0],
  "macarons",
  {
    renderer: "canvas"
  }
);

lineChart.setOption(option);

let testHistogram;
fetch("/histogram", {
  method: "GET"
})
  .then(res => {
    console.log(res);
    return res.json();
  })
  .then(data => {
    // console.log(data);
    testHistogram = data;
    line_chart(lineChart, data, option);
    lineChart.resize();
  })
  .catch(err => {
    console.log(err);
  });

// const chartColor = lineChart["_theme"]["color"];
let series = [];
function line_chart(chart, data, option) {
  console.log(data);
  // chart.clear();

  //차트 옵션 설정
  const dateHistogram = Object.keys(data);
  for (let i = 0; i < dateHistogram.length; i++) {
    dateHistogram[i] = dateHistogram[i].substring(0, 7);
  }
  option.xAxis = {
    // data: Object.keys(data)
    data: dateHistogram
  };

  option.series[0].data = Object.values(data);

  chart.setOption(option);
}

//차트 resize
window.onresize = () => {
  if (lineChart != null && lineChart != undefined) {
    lineChart.resize();
  }
};