/**
 * 性能监控工具
 */

// 页面加载性能监控
export const trackPagePerformance = (pageName: string) => {
  if (typeof window === 'undefined') return

  // 使用 Performance API 监控页面性能
  const observer = new PerformanceObserver(list => {
    const entries = list.getEntries()
    entries.forEach(entry => {
      if (entry.entryType === 'navigation') {
        const navigationEntry = entry as PerformanceNavigationTiming
        console.log(`[Performance] ${pageName}:`, {
          dns: navigationEntry.domainLookupEnd - navigationEntry.domainLookupStart,
          tcp: navigationEntry.connectEnd - navigationEntry.connectStart,
          ttfb: navigationEntry.responseStart - navigationEntry.requestStart,
          domContentLoaded:
            navigationEntry.domContentLoadedEventEnd - navigationEntry.navigationStart,
          load: navigationEntry.loadEventEnd - navigationEntry.navigationStart
        })
      }
    })
  })

  observer.observe({ entryTypes: ['navigation'] })

  // 清理监听器
  return () => observer.disconnect()
}

// 组件渲染性能监控
export const trackComponentPerformance = (componentName: string) => {
  const startTime = performance.now()

  return () => {
    const endTime = performance.now()
    const renderTime = endTime - startTime

    if (renderTime > 16) {
      // 超过一帧时间(16ms)的渲染
      console.warn(`[Performance Warning] ${componentName} render time: ${renderTime.toFixed(2)}ms`)
    }
  }
}

// 资源加载监控
export const trackResourceLoading = () => {
  if (typeof window === 'undefined') return

  const observer = new PerformanceObserver(list => {
    const entries = list.getEntries()
    entries.forEach(entry => {
      if (entry.entryType === 'resource') {
        const resourceEntry = entry as PerformanceResourceTiming
        const loadTime = resourceEntry.responseEnd - resourceEntry.startTime

        // 监控大文件加载
        if (loadTime > 1000) {
          // 超过1秒的资源加载
          console.warn(
            `[Performance Warning] Slow resource: ${resourceEntry.name}, load time: ${loadTime.toFixed(2)}ms`
          )
        }
      }
    })
  })

  observer.observe({ entryTypes: ['resource'] })
  return () => observer.disconnect()
}

// 内存使用监控
export const trackMemoryUsage = () => {
  if (typeof window === 'undefined' || !('memory' in performance)) return

  const memory = (performance as any).memory
  return {
    used: Math.round(memory.usedJSHeapSize / 1048576), // MB
    total: Math.round(memory.totalJSHeapSize / 1048576), // MB
    limit: Math.round(memory.jsHeapSizeLimit / 1048576) // MB
  }
}

// 防抖函数
export const debounce = <T extends (...args: any[]) => any>(
  func: T,
  wait: number
): ((...args: Parameters<T>) => void) => {
  let timeout: NodeJS.Timeout | null = null

  return (...args: Parameters<T>) => {
    if (timeout) clearTimeout(timeout)
    timeout = setTimeout(() => func(...args), wait)
  }
}

// 节流函数
export const throttle = <T extends (...args: any[]) => any>(
  func: T,
  limit: number
): ((...args: Parameters<T>) => void) => {
  let inThrottle: boolean = false

  return (...args: Parameters<T>) => {
    if (!inThrottle) {
      func(...args)
      inThrottle = true
      setTimeout(() => (inThrottle = false), limit)
    }
  }
}

// 图片懒加载指令
export const lazyLoad = {
  mounted(el: HTMLImageElement, binding: { value: string }) {
    const observer = new IntersectionObserver(entries => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          const img = entry.target as HTMLImageElement
          img.src = binding.value
          img.classList.remove('lazy')
          observer.unobserve(img)
        }
      })
    })

    el.classList.add('lazy')
    observer.observe(el)
  }
}

// 错误边界工具
export const createErrorHandler = (componentName: string) => {
  return (error: Error, vm: any, info: string) => {
    console.error(`[Error Boundary] ${componentName}:`, {
      error: error.message,
      stack: error.stack,
      info,
      vm: vm?.$options?.name || 'Unknown'
    })

    // 可以在这里上报错误到监控服务
    // reportError(error, componentName, info)
  }
}
