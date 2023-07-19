import { useEffect, useRef } from "react"

export const useTitleSwitcher = () => {

  const titleSwitchOptions = [
    {
      text: 'Big nest forum',
    },
    {
      text: 'Post anything...',
    },
  ]

  const titleSwitchStateRef = useRef({ index: 0, option: titleSwitchOptions[0] })
  const setTitleSwitchState = value => {
    titleSwitchStateRef.current = value
  }

  const nextOption = () => {
    const lastIndex = titleSwitchOptions.length - 1
    const currentOption = titleSwitchStateRef.current
    if (currentOption.index == lastIndex) {
      return { index: 0, option: titleSwitchOptions[0] }
    }
    return { 
      index: currentOption.index + 1, 
      option: titleSwitchOptions[currentOption.index + 1] 
    }
  }

  useEffect(() => {
    const interval = setInterval(() => {
      const next = nextOption()
      setTitleSwitchState(next)
      document.title = next.option.text
    }, 2000)
    return () => {
      clearInterval(interval)
    }
  }, [])
}