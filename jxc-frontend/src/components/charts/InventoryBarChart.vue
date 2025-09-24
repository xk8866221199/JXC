<template>
  <div class="inventory-bar-chart">
    <VChart :option="chartOption" :style="{ height: height, width: '100%' }" autoresize />
  </div>
</template>

<script setup lang="ts">
  import { computed, type PropType } from 'vue'

  // 接口类型定义
  interface InventoryData {
    name: string
    current: number
    min: number
    max: number
    status: 'normal' | 'warning' | 'danger'
  }

  // 属性定义
  const props = defineProps({
    data: {
      type: Array as PropType<InventoryData[]>,
      required: true
    },
    height: {
      type: String,
      default: '400px'
    },
    title: {
      type: String,
      default: '库存预警监控'
    }
  })

  // 获取状态颜色
  const getStatusColor = (status: string) => {
    const colors = {
      normal: '#52c41a',
      warning: '#faad14',
      danger: '#f5222d'
    }
    return colors[status as keyof typeof colors] || '#1890ff'
  }

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
        type: 'shadow'
      },
      formatter: (params: any[]) => {
        const data = props.data[params[0].dataIndex]
        return `
        <div style="margin-bottom: 8px; font-weight: bold;">${data.name}</div>
        <div style="margin-bottom: 4px;">
          当前库存: <span style="font-weight: bold; color: ${getStatusColor(data.status)};">${data.current}</span>
        </div>
        <div style="margin-bottom: 4px;">
          最小库存: <span style="color: #faad14;">${data.min}</span>
        </div>
        <div>
          最大库存: <span style="color: #52c41a;">${data.max}</span>
        </div>
      `
      }
    },
    legend: {
      data: ['当前库存', '最小库存', '最大库存'],
      top: 30
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '10%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: props.data.map(item => item.name),
      axisLabel: {
        rotate: 45,
        interval: 0
      }
    },
    yAxis: {
      type: 'value',
      name: '数量',
      axisLabel: {
        formatter: '{value}'
      }
    },
    series: [
      {
        name: '当前库存',
        type: 'bar',
        data: props.data.map(item => ({
          value: item.current,
          itemStyle: {
            color: getStatusColor(item.status)
          }
        })),
        barWidth: '30%'
      },
      {
        name: '最小库存',
        type: 'line',
        data: props.data.map(item => item.min),
        lineStyle: {
          color: '#faad14',
          type: 'dashed',
          width: 2
        },
        symbol: 'circle',
        symbolSize: 6
      },
      {
        name: '最大库存',
        type: 'line',
        data: props.data.map(item => item.max),
        lineStyle: {
          color: '#52c41a',
          type: 'dashed',
          width: 2
        },
        symbol: 'circle',
        symbolSize: 6
      }
    ]
  }))
</script>

<style scoped>
  .inventory-bar-chart {
    width: 100%;
    background: #fff;
    border-radius: 8px;
    padding: 16px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
</style>
