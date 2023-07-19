import axios from "axios";
import config from '../config'

export const defaultFetcher = reqUrl => axios(`${config.serverUrl}${reqUrl}`).then(res => res.data)

export function createAuthHeaders() {
  const accessToken = localStorage.getItem('accessToken')
  if (!accessToken) {
    return undefined
  }
  return { 'Authorization': `Bearer ${accessToken}` }
}