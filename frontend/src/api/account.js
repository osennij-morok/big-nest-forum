import axios, { HttpStatusCode } from "axios"
import jwtDecode from "jwt-decode"
import config from '../config'
import * as api from './api'


export async function signIn(username, password, captchaToken) {
  // const storedAccount = assertCanSignIn(username, password)
  const requestBody = { username, password, captchaToken }
  const response = await axios.post(`${config.serverUrl}/signIn`, requestBody)
    // .then(res => res.data)
  switch (response.status) {
    case HttpStatusCode.BadRequest:
      throw {
        message: "Требуется капча",
        code: "MISSING_CAPTCHA",
        status: response.status,
      }
    case HttpStatusCode.Forbidden:
      throw {
        message: "Требуется перепройти капчу",
        code: "INVALID_CAPTCHA",
        status: response.status,
      }
    case HttpStatusCode.Ok:
      const decodedJwt = jwtDecode(response.data.accessToken)
      const userData = {
        ...response.data,
        username,
        role: decodedJwt.role,
      }
      localStorage.setItem("accessToken", userData.accessToken)
      localStorage.setItem("refreshToken", userData.refreshToken)
      localStorage.setItem("username", userData.username)
      localStorage.setItem("role", userData.role)
      return userData
    default:
      throw {
        message: "Произошла ошибка",
        code: "OTHER_ERROR",
        status: response.status,
      }
  }
}

export async function signOut() {
  localStorage.removeItem("accessToken")
  localStorage.removeItem("refreshToken")
  localStorage.removeItem("username")
  localStorage.removeItem("role")
}

export async function signUp(username, password, captchaToken) {
  const requestBody = { username, password, captchaToken }
  const response = await axios.post(`${config.serverUrl}/signUp`, requestBody)
  switch (response.status) {
    case HttpStatusCode.Forbidden:
      throw {
        message: "Пользователь с таким именем уже существует",
        code: "USER_ALREADY_EXISTS",
        status: response.status,
      }
    case HttpStatusCode.Ok:
      return
    default:
      throw {
        message: "Произошла ошибка",
        code: "OTHER_ERROR",
        status: response.status,
      }
  }
  // assertNotExists(username)
  // addAccount(username, password)
}

export async function refresh() {
  const requestBody = { refreshToken: localStorage.getItem('refreshToken') }
  try {
    const refreshResponse = await axios.post(`${config.serverUrl}/refresh`, requestBody)
    localStorage.setItem('accessToken', refreshResponse.data.accessToken)
  } catch (err) {
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    localStorage.removeItem('username')
    localStorage.removeItem('role')
    throw {
      message: "Сессия истекла, требуется войти в систему заново",
      code: 'SESSION_EXPIRED',
      parent: err,
    }
  }
}

export async function changePassword(newPassword) {
  const reqConfig = { headers: api.createAuthHeaders() }
  try {
    await axios.put(`${config.serverUrl}/account/password`, { newPassword }, reqConfig)
  } catch (err) {
    if (err.response.status == HttpStatusCode.Forbidden) {
      await refresh()
      const reqConfig = { headers: api.createAuthHeaders() }
      try {
        await axios.put(`${config.serverUrl}/account/password`, { newPassword }, reqConfig)
      } catch (err) {
        throw {
          message: "Невозможно сменить пароль",
          code: 'OTHER_ERROR',
          parent: err,
        }
      }
      return
    }
    throw {
      message: "Невозможно сменить пароль",
      code: 'OTHER_ERROR',
      parent: err,
    }
  }
}

export async function getAll() {
  const reqConfig = { headers: api.createAuthHeaders() }
  try {
    return (await axios.get(`${config.serverUrl}/account`, reqConfig)).data
  } catch (err) {
    if (err.response.status == HttpStatusCode.Forbidden) {
      await refresh()
      const reqConfig = { headers: api.createAuthHeaders() }
      try {
        return (await axios.get(`${config.serverUrl}/account`, reqConfig)).data
      } catch (err) {
        throw {
          message: "Невозможно получить список аккаунтов",
          code: 'OTHER_ERROR',
          parent: err,
        }
      }
    }
    throw {
      message: "Невозможно получить список аккаунтов",
      code: 'OTHER_ERROR',
      parent: err,
    }
  }
}

export async function blockAccount(accountId) {
  const url = `${config.serverUrl}/account/${accountId}/blocking`
  const reqConfig = { headers: api.createAuthHeaders() }
  try {
    return (await axios.post(url, {}, reqConfig)).data
  } catch (err) {
    if (err.response.status == HttpStatusCode.Forbidden) {
      await refresh()
      const reqConfig = { headers: api.createAuthHeaders() }
      try {
        return (await axios.post(url, {}, reqConfig)).data
      } catch (err) {
        throw {
          message: "Невозможно заблокировать аккаунт",
          code: 'OTHER_ERROR',
          parent: err,
        }
      }
    }
    throw {
      message: "Невозможно заблокировать аккаунт",
      code: 'OTHER_ERROR',
      parent: err,
    }
  }
}

export async function unblockAccount(accountId) {
  const url = `${config.serverUrl}/account/${accountId}/blocking`
  const reqConfig = { headers: api.createAuthHeaders() }
  try {
    return (await axios.delete(url, reqConfig)).data
  } catch (err) {
    if (err.response.status == HttpStatusCode.Forbidden) {
      await refresh()
      const reqConfig = { headers: api.createAuthHeaders() }
      try {
        return (await axios.delete(url, reqConfig)).data
      } catch (err) {
        throw {
          message: "Невозможно разблокировать аккаунт",
          code: 'OTHER_ERROR',
          parent: err,
        }
      }
    }
    throw {
      message: "Невозможно разблокировать аккаунт",
      code: 'OTHER_ERROR',
      parent: err,
    }
  }
}

export async function changeRole(accountId, targetRole) {
  const url = `${config.serverUrl}/account/${accountId}/role`
  const reqConfig = { headers: api.createAuthHeaders() }
  try {
    return (await axios.put(url, { role: targetRole }, reqConfig)).data
  } catch (err) {
    if (err.response.status == HttpStatusCode.Forbidden) {
      await refresh()
      const reqConfig = { headers: api.createAuthHeaders() }
      try {
        return (await axios.put(url, { role: targetRole }, reqConfig)).data
      } catch (err) {
        throw {
          message: "Невозможно изменить роль",
          code: 'OTHER_ERROR',
          parent: err,
        }
      }
    }
    throw {
      message: "Невозможно изменить роль",
      code: 'OTHER_ERROR',
      parent: err,
    }
  }
}

// const accounts = [
//   {
//     id: 1,
//     username: 'john1111',
//     password: 's333crett',
//     role: 'ADMIN',
//   },
//   {
//     id: 2,
//     username: 'andy3333',
//     password: 's33cret',
//     role: 'USER',
//   },
// ]

// function assertNotExists(username) {
//   const storedAccount = accounts.find(account => account.username === username)
//   if (storedAccount) {
//     throw {
//       message: "Account with such username already exists",
//       code: "USERNAME_ALREADY_EXISTS",
//     }
//   }
// }

// function assertCanSignIn(username, password) {
//   const storedAccount = accounts.find(account => account.username === username)
//   if (!storedAccount) {
//     throw {
//       message: "Account with such username does not exist",
//       code: "USERNAME_NOT_EXIST",
//     }
//   }
//   if (storedAccount.password != password) {
//     throw {
//       message: "Incorrect password",
//       code: "INCORRECT_PASSWORD,"
//     }
//   }
//   return storedAccount
// }

// function nextId() {
//   return accounts[accounts.length - 1].id + 1
// }

// function addAccount(username, password) {
//   accounts.push({
//     id: nextId(),
//     username,
//     password,
//     role: 'USER',
//   })
// }