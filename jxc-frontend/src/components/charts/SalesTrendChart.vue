<template>
  <div class="sales-trend-chart">
    <VChart :option="chartOption" :style="{ height: height, width: '100%' }" autoresize />
  </div>
</template>

<script setup lang="ts">
  import { computed, type PropType } from 'vue'

  // 接口类型定义
  interface SalesData {
    date: string
    sales: number
    profit: number
  }

  // 属性定义
  const props = defineProps({
    data: {
      type: Array as PropType<SalesData[]>,
      required: true
    },
    height: {
      type: String,
      default: '400px'
    },
    title: {
      type: String,
      default: '销售趋势'
    }
  })

  // 图表配置
  const chartOption = computed(() => ({
    title: {
      text: props.title,
      left: 'center',
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold'
      }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        label: {
          backgroundColor: '#6a7985'
        }
      },
      formatter: (params: any[]) => {
        let html = `<div style="margin-bottom: 8px;">${params[0].axisValue}</div>`
        params.forEach(param => {
          const unit = param.seriesName === '销售额' ? '元' : '元'
          html += `
          <div style="margin-bottom: 4px;">
            ${param.marker} ${param.seriesName}: 
            <span style="font-weight: bold; color: ${param.color};">
              ¥${param.value.toLocaleString()}${unit}
            </span>
          </div>
        `
        })
        return html
      }
    },
    legend: {
      data: ['销售额', '利润'],
      top: 30
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: props.data.map(item => item.date),
      axisLabel: {
        rotate: 45
      }
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '¥{value}'
      }
    },
    series: [
      {
        name: '销售额',
        type: 'line',
        stack: 'Total',
        smooth: true,
        lineStyle: {
          width: 3
        },
        areaStyle: {
          opacity: 0.3
        },
        data: props.data.map(item => item.sales),
        itemStyle: {
          color: '#1890ff'
        }
      },
      {
        name: '利润',
        type: 'line',
        stack: 'Total',
        smooth: true,
        lineStyle: {
          width: 3
        },
        areaStyle: {
          opacity: 0.3
        },
        data: props.data.map(item => item.profit),
        itemStyle: {
          color: '#52c41a'
        }
      }
    ]
  }))
</script>

<style scoped>
  .sales-trend-chart {
    width: 100%;
    background: #fff;
    border-radius: 8px;
    padding: 16px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
</style>
