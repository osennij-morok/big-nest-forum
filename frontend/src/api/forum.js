import axios, { HttpStatusCode } from "axios";
import config from "../config"
import useSWR from "swr"
import { refresh } from "./account";
import * as api from '../api/api'

const forums = [
  {
    id: 'f',
    name: 'Flooding',
    categoryId: 1,
  },
  {
    id: 'g',
    name: 'Games',
    categoryId: 2,
  },
  {
    id: 'wr',
    name: 'wr',
    categoryId: 2,
  },
]

export function useForums() {
  const fetcher = reqUrl => axios(`${config.serverUrl}${reqUrl}`).then(res => res.data)
  // const tmpFetcher = async reqUrl => new Promise(resolve => setTimeout(() => resolve(forums), 1000))
  const { data, error, isLoading, mutate } = useSWR('/forum', fetcher)
  return { forums: data, error, isLoading, mutate }
}

export function useForum(forumId) {
  // const tmpFetcher = async reqUrl => {
  //   const foundForum = forums.find(forum => forum.id == forumId)
  //   if (!foundForum) {
  //     throw Error("Форума с таким именем не существует")
  //   }
  //   return foundForum
  // }
  const fetcher = async reqUrl => axios.get(`${config.serverUrl}${reqUrl}`).then(res => res.data)
  const { data, error, isLoading, mutate } = useSWR(`/forum/${forumId}`, fetcher)
  return { forum: data, error, isLoading, mutate }
}

export async function createForum(forum) {
  try {
    await axios.post(`${config.serverUrl}/forum`, forum, { headers: api.createAuthHeaders() })
  } catch (err) {
    if (err.response.status == HttpStatusCode.Forbidden) {
      await refresh()
      try {
        await axios.post(url, { headers: api.createAuthHeaders() })
      } catch (err) {
        throw {
          message: "Невозможно создать форум",
          code: 'OTHER_ERROR',
          parent: err,
        }
      }
      return
    }
    if (err.response.status == HttpStatusCode.BadRequest) {
      throw {
        message: "Невозможно создать форум",
        code: 'INVALID_INPUT',
        parent: err,
      }
    }
  }
}