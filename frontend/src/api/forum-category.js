import axios from "axios";
import config from "../config"
import useSWR from "swr"

export function useCategories() {
  const fetcher = reqUrl => axios.get(`${config.serverUrl}${reqUrl}`).then(res => res.data)
  const { data, error, isLoading, mutate } = useSWR('/category', fetcher)
  return { categories: data, error, isLoading, mutate }
}