import HCaptcha from "@hcaptcha/react-hcaptcha"
import { yupResolver } from "@hookform/resolvers/yup"
import { HttpStatusCode } from "axios"
import { useEffect } from "react"
import { useRef } from "react"
import { useState } from "react"
import { useForm } from "react-hook-form"
import { Navigate } from "react-router-dom"
import * as yup from "yup"
import { signIn } from "../api/account"
import config from "../config"
import { PasswordField, TextField } from "../forms/input-field"
import SubmitField from "../forms/submit-field"
import { useAuthStore } from "../store/authentication"

export default function SignIn() {

  useEffect(() => {
    console.log(`Sitekey: ${config.hcaptchaSitekey}`)
  }, [])

  const signInFormScheme = yup.object({
    username: yup.string()
      .trim()
      .required('Требуется имя пользователя'),
    password: yup.string()
      .trim()
      .required('Требуется пароль')
  })

  const { register, formState: { errors }, handleSubmit } = useForm({
    resolver: yupResolver(signInFormScheme)
  })

  const [isLoading, setLoading] = useState(false)
  const [captchaToken, setCaptchaToken] = useState(null)
  const captchaRef = useRef(null)
  const [additionalError, setAdditionalError] = useState(null)

  const isAuthenticated = useAuthStore((state) => state.isAuthenticated)
  const storeOnSignIn = useAuthStore((state) => state.onSignedIn)

  const prohibitSigningIn = !config.allowUsersAuthentication || isAuthenticated

  const onSubmit = async data => {
    setAdditionalError(null)
    if (!captchaToken && config.requireCaptcha) {
      setAdditionalError("Требуется пройти капчу")
      return
    }
    setLoading(true)
    try {
      const accountData = await new Promise(
        (resolve) => setTimeout(() => resolve(signIn(data.username, data.password, captchaToken)), 1000))
      storeOnSignIn(accountData.username, accountData.role)
    } catch (err) {
      if (err.response.status == HttpStatusCode.Forbidden) {
        switch (err.response.data?.code) {
          case 'BAD_CREDENTIALS':
            setAdditionalError("Неверное имя пользователя или пароль")
            return
          case 'INVALID_CAPTCHA_TOKEN':
            setAdditionalError("Требуется повторно пройти капчу")
            return
          case 'BLOCKED_ACCOUNT':
            setAdditionalError("Аккаунт заблокирован")
            return
        }
      }
      setAdditionalError(err.message)
      return
    } finally {
      setLoading(false)
      captchaRef.current?.resetCaptcha()
      console.log(`Капча должна быть сброшена...`)
    }
  }

  const captchaSendVerifyRequest = async (token, ekey) => {
    setAdditionalError(null)
    setCaptchaToken(token)
    // console.log(`token: \n${token}`)
    // console.log(`ekey: ${ekey}`)
    // const verification = await verifyCaptcha(token, ekey)
    // setCaptchaVerified(verification.status)
  }

  const onChallengeExpired = () => {
    setCaptchaToken(null)
  }

  // const canSignIn = (username.trim().length != null)
  //   && (password.trim().length != null)

  const signInForm = <div className="column">
    <div className="columns is-centered">
      <div className="column is-narrow box content">
        <h1>Вход</h1>
        <form onSubmit={handleSubmit(onSubmit)}>
          <TextField label="Имя пользователя"
            name="username"
            register={register}
            errors={errors} />
          <PasswordField label="Пароль"
            name="password"
            register={register}
            errors={errors} />
          {config.requireCaptcha && (
            <HCaptcha sitekey={config.hcaptchaSitekey}
              ref={captchaRef}
              onVerify={captchaSendVerifyRequest}
              onExpire={onChallengeExpired}
            />
          )}
          <SubmitField text="Войти" isLoading={isLoading} />
          {additionalError && (
            <p className="help is-danger">{additionalError}</p>
          )}
        </form>
      </div>
    </div>
  </div>


  return <div className="columns is-centered">
    {prohibitSigningIn && <Navigate to="/forums" />}
    {!prohibitSigningIn && signInForm}
  </div>
}