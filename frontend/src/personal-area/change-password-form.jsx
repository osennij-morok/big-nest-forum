import { useForm } from "react-hook-form"
import { yupResolver } from '@hookform/resolvers/yup'
import * as yup from 'yup'
import { PasswordField } from '../forms/input-field'
import SubmitField from '../forms/submit-field'
import { useState } from "react"
import * as account from '../api/account'

export default function ChangePasswordForm() {

  const changePasswordDataModel = yup.object({
    password: yup.string()
      .trim()
      .required('Требуется ввести пароль'),
    repeatedPassword: yup.string()
      .trim()
      .oneOf([yup.ref('password')], 'Пароли не совпадают')
      .required('Требуется повторить пароль'),
  })

  const { 
    handleSubmit, 
    register, 
    reset: resetFormFields,
    formState: { errors } 
  } = useForm({ resolver: yupResolver(changePasswordDataModel) })

  const [isLoading, setIsLoading] = useState(false)
  const [additionalError, setAdditionalError] = useState(null)
  const [successMessage, setSuccessMessage] = useState(null)

  const onSubmit = async (formData) => {
    setIsLoading(true)
    setAdditionalError(null)
    setSuccessMessage(null)
    console.log(formData)
    try {
      await account.changePassword(formData.password)
      setSuccessMessage('Пароль успешно изменён')
    } catch (err) {
      setAdditionalError(err.message)
      return
    } finally {
      setIsLoading(false)
    }
    resetFormFields()
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} >
      <PasswordField 
        name="password"
        label="Новый пароль"
        register={register}
        errors={errors} />
      <PasswordField 
        name="repeatedPassword"
        label="Повтор пароля"
        register={register}
        errors={errors} />
      <SubmitField text="Сменить пароль" isLoading={isLoading} />
      {additionalError && (
        <p className="help is-danger">{additionalError}</p>
      )}
      {successMessage && (
        <p className="help is-success">{successMessage}</p>
      )}
    </form>
  )
}