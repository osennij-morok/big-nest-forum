import axios from 'axios'
import useSWR from 'swr'
import config from '../config'
import * as api from './api'

export function useThreads(forumId) {
  const { 
    data, 
    error,
    isLoading,
    mutate 
  } = useSWR(`/thread?forumId=${forumId}`, api.defaultFetcher)
  return {
    threads: data,
    isLoading,
    error,
    mutate
  }
}

export async function createThread(forumId, thread) {
  return await axios.post(`${config.serverUrl}/${forumId}/thread`, thread)
}

// export default { useThreads, }