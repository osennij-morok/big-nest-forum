import { useForm } from "react-hook-form"
import { object, ref, string } from "yup"
import { useAuthStore } from "../store/authentication"
import { yupResolver } from '@hookform/resolvers/yup'
import { PasswordField, TextField } from "../forms/input-field"
import SubmitField from "../forms/submit-field"
import config from "../config"
import { Navigate, useNavigate } from "react-router-dom"
import { signUp } from "../api/account"
import { useRef, useState } from "react"
import HCaptcha from "@hcaptcha/react-hcaptcha"

export default function SignUp() {

  const signUpFormSchema = object({
    username: string()
      .trim()
      .required('Требуется имя пользователя'),
    password: string()
      .trim()
      .required('Требуется пароль'),
    confirmPassword: string()
      .trim()
      .required('Требуется повторить пароль')
      .oneOf([ref('password'), null], 'Пароли не совпадают'),
  }).required()

  const { register, handleSubmit, formState: { errors } } = useForm({
    resolver: yupResolver(signUpFormSchema)
  })
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated)
  const navigate = useNavigate()

  const captchaRef = useRef(null)
  const [captchaToken, setCaptchaToken] = useState(null)
  
  const [additionalError, setAdditionalError] = useState(null)
  const [isLoading, setLoading] = useState(false)

  const pageIsAllowed = !isAuthenticated && config.allowUsersAuthentication

  const onSubmit = async data => {
    console.log(data)
    setAdditionalError(null)
    setLoading(true)
    if (!captchaToken && config.requireCaptcha) {
      setAdditionalError("Требуется пройти капчу")
      setLoading(false)
      return
    }
    try {
      await new Promise(
        (resolve) => setTimeout(() => resolve(signUp(data.username, data.password, captchaToken)), 1000))
    } catch (err) {
      if (err.response.status == HttpStatusCode.Forbidden) {
        switch (err.response.data?.code) {
          case 'ACCOUNT_ALREADY_EXISTS':
            setAdditionalError("Неверное имя пользователя или пароль")
            return
          case 'INVALID_CAPTCHA_TOKEN':
            setAdditionalError("Требуется повторно пройти капчу")
            return
        }
      }
      setAdditionalError(err.message)
      return
    } finally {
      setLoading(false)
      captchaRef.current?.resetCaptcha()
    }
    navigate('/signIn')
  }

  const captchaSendVerifyRequest = async (token, ekey) => {
    setAdditionalError(null)
    setCaptchaToken(token)
  }

  const onChallengeExpired = () => {
    setCaptchaToken(null)
  }

  const registrationForm = <>
    <div className="column">
      <div className="columns is-centered">
        <div className="column is-narrow box content">
          <h1>Регистрация</h1>
          <form onSubmit={handleSubmit(onSubmit)}>
            <TextField label="Имя пользователя"
              name="username"
              register={register}
              errors={errors} />
            <PasswordField label="Пароль"
              name="password"
              register={register}
              errors={errors} />
            <PasswordField label="Подтвердите пароль"
              name="confirmPassword"
              register={register}
              errors={errors} />
            {config.requireCaptcha && (
              <HCaptcha sitekey={config.hcaptchaSitekey}
                ref={captchaRef}
                onVerify={captchaSendVerifyRequest}
                onExpire={onChallengeExpired}
              />
            )}
            <SubmitField text="Зарегистрироваться" isLoading={isLoading} />
            {additionalError && (
              <p className="help is-danger">{additionalError}</p>
            )}
          </form>
        </div>
      </div>
    </div>
  </>

  return (
    <>
      {!pageIsAllowed && <Navigate to="/forums" />}
      {pageIsAllowed && registrationForm}
    </>
  )
}