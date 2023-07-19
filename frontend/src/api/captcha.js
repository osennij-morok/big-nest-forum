import axios from "axios";
import config from "../config";

export async function verifyCaptcha(token, ekey) {
  return axios
    .get(`${config.serverUrl}/captcha/verification?token=${token}&ekey=${ekey}`)
    .then(res => res.data)
}