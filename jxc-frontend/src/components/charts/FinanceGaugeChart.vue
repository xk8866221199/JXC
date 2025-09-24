<template>
  <div class="finance-gauge-chart">
    <VChart :option="chartOption" :style="{ height: height, width: '100%' }" autoresize />
  </div>
</template>

<script setup lang="ts">
  import { computed, type PropType } from 'vue'

  // 接口类型定义
  interface GaugeData {
    name: string
    value: number
    max: number
    unit: string
    color?: string
  }

  // 属性定义
  const props = defineProps({
    data: {
      type: Object as PropType<GaugeData>,
      required: true
    },
    height: {
      type: String,
      default: '300px'
    },
    title: {
      type: String,
      default: '财务指标'
    }
  })

  // 获取颜色
  const getColor = (value: number, max: number) => {
    const ratio = value / max
    if (ratio < 0.3) return '#52c41a'
    if (ratio < 0.7) return '#faad14'
    return '#f5222d'
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
    series: [
      {
        name: props.data.name,
        type: 'gauge',
        center: ['50%', '60%'],
        startAngle: 200,
        endAngle: -40,
        min: 0,
        max: props.data.max,
        splitNumber: 5,
        itemStyle: {
          color: props.data.color || getColor(props.data.value, props.data.max)
        },
        progress: {
          show: true,
          width: 20
        },
        pointer: {
          show: false
        },
        axisLine: {
          lineStyle: {
            width: 20
          }
        },
        axisTick: {
          distance: -45,
          splitNumber: 5,
          lineStyle: {
            width: 2,
            color: '#999'
          }
        },
        splitLine: {
          distance: -52,
          length: 14,
          lineStyle: {
            width: 3,
            color: '#999'
          }
        },
        axisLabel: {
          distance: -20,
          color: '#999',
          fontSize: 12,
          formatter: (value: number) => {
            if (value >= 10000) {
              return `${(value / 10000).toFixed(1)}万`
            }
            return value.toString()
          }
        },
        anchor: {
          show: false
        },
        title: {
          show: false
        },
        detail: {
          valueAnimation: true,
          width: '60%',
          lineHeight: 40,
          borderRadius: 8,
          offsetCenter: [0, '-15%'],
          fontSize: 24,
          fontWeight: 'bold',
          formatter: () => {
            const value =
              props.data.value >= 10000
                ? `${(props.data.value / 10000).toFixed(1)}万`
                : props.data.value.toLocaleString()
            return `{value|${value}}\n{name|${props.data.name}}`
          },
          rich: {
            value: {
              fontSize: 28,
              fontWeight: 'bold',
              color: props.data.color || getColor(props.data.value, props.data.max)
            },
            name: {
              fontSize: 14,
              color: '#666',
              padding: [4, 0, 0, 0]
            }
          }
        },
        data: [
          {
            value: props.data.value,
            name: props.data.name
          }
        ]
      }
    ]
  }))
</script>

<style scoped>
  .finance-gauge-chart {
    width: 100%;
    background: #fff;
    border-radius: 8px;
    padding: 16px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
</style>
