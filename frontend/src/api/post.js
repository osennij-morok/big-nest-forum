import axios, { HttpStatusCode } from 'axios'
import { useRef } from 'react'
import { useEffect } from 'react'
import { useState } from 'react'
import useSWR from 'swr'
import config from '../config'
import { refresh } from './account'
import * as api from './api'

export function usePost(forumId, postId) {
  const { 
    data, 
    error, 
    isLoading, 
    mutate 
  } = useSWR(`/post?forumId=${forumId}&postId=${postId}`, api.defaultFetcher)
  return {
    post: data,
    isLoading,
    error,
    mutate
  }
}

export function usePosts(forumId, threadId, fromId) {
  const { 
    data, 
    error, 
    isLoading, 
    mutate 
  } = useSWR(`/post?forumId=${forumId}&threadId=${threadId}&fromId=${fromId}`, api.defaultFetcher)
  return { posts: data, error, isLoading, mutate }
}

export function usePostsPage(forumId, threadId, fromId, defaultLimit) {

  const [totalPosts, setTotalPosts] = useState([])
  const [isLoading, setLoading] = useState(false)
  const [totalCount, setTotalCount] = useState(0)
  const [postsLimit, setPostsLimit] = useState(defaultLimit)
  const [error, setError] = useState(null)
  const abortControllerRef = useRef(new AbortController())

  const lastPostId = useRef(0)
  const setLastPostId = value => {
    lastPostId.current = value
  }

  const fullUpdateDoneRef = useRef(false)
  const setFullUpdateDone = value => {
    fullUpdateDoneRef.current = value
  }

  const updatePosts = async () => {
    setFullUpdateDone(true)
    const query = `/post?forumId=${forumId}&threadId=${threadId}`
    setLoading(true)
    try {
      const response = await axios.get(`${config.serverUrl}${query}`)
      if (response.data?.length == 0) {
        setTotalPosts([])
        setTotalCount(0);
        setPostsLimit(defaultLimit)
        return
      }
      setTotalPosts(response.data)
      setLastPostId(response.data.at(-1)?.id)
      setTotalCount(response.data.length);
      setPostsLimit(defaultLimit)
    } catch (error) {
      setError(error)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    const query = `/post/pages?forumId=${forumId}&threadId=${threadId}&fromId=${fromId}&limit=${postsLimit}`
    const abortController = abortControllerRef.current
    async function doRequest() {
      if (fullUpdateDoneRef.current) {
        setFullUpdateDone(false)
        return
      }
      try {
        setLoading(true)
        const response = await axios.get(`${config.serverUrl}${query}`, {
          signal: abortController.signal
        })
        if (response.data?.data.length > 0) {
          // setTotalPosts((prev) => [...prev, ...response.data])
          setTotalPosts([...totalPosts, ...response.data.data])
          setLastPostId(response.data.data.at(-1)?.id)
          setTotalCount(response.data.totalCount);
          setPostsLimit(defaultLimit)
        }
      } catch (err) {
        setError(err)
      } finally {
        setLoading(false)
      }
    }
    doRequest()
    // return () => abortController.abort()
  }, [fromId])
  
  return { 
    posts: totalPosts, 
    isLoading, 
    error,
    lastPostId,
    totalCount,
    setPostsLimit,
    updatePosts,
  }
}

export async function createPost(post, forumId) {
  const headers = api.createAuthHeaders()
  const requestConfig = headers ? { headers } : undefined
  const url = `${config.serverUrl}/${forumId}/post`
  try {
    return await axios.post(url, post, requestConfig)
  } catch(err) {
    if (err.response.status == HttpStatusCode.Forbidden) {
      await refresh()
      const headers = api.createAuthHeaders()
      const requestConfig = headers ? { headers } : undefined
      try {
        return await axios.post(url, post, requestConfig)
      } catch (err) {
        throw {
          message: "Невозможно создать пост",
          code: 'OTHER_ERROR',
          parent: err,
        }
      }
    }
    throw err
  }
}

export async function deletePost(forumId, postId) {
  const url = `${config.serverUrl}/${forumId}/post?postId=${postId}`
  try {
    await axios.delete(url, { headers: api.createAuthHeaders() })
  } catch (err) {
    await refresh()
    try {
      await axios.delete(url, { headers: api.createAuthHeaders() })
    } catch (err) {
      throw {
        message: "Невозможно провести удаление",
        code: 'OTHER_ERROR',
        parent: err,
      }
    }
  }
}