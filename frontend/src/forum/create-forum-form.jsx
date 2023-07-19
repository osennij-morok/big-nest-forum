import { yupResolver } from "@hookform/resolvers/yup"
import classNames from "classnames"
import { useState } from "react"
import { useForm } from "react-hook-form"
import * as yup from "yup"
import { createForum, useForums } from "../api/forum"
import { useCategories } from "../api/forum-category"
import { InputField } from "../forms/input-field"
import SelectField from "../forms/select-field"
import SubmitField from "../forms/submit-field"

export default function CreateForumForm() {

  const postFormScheme = yup.object({
    id: yup.string()
      .trim()
      .required('Требуется ввести ID'),
    name: yup.string()
      .trim()
      .required('Требуется ввести название форума'),
    categoryId: yup.number()
      .positive('Требуется выбрать категорию'),
  })

  const { register, formState: { errors }, handleSubmit, reset: resetForm } = useForm({ 
    resolver: yupResolver(postFormScheme) 
  })
  const [isLoading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const { 
    categories, 
    isLoading: categoriesAreLoading, 
    error: categoriesError 
  } = useCategories()
  const { mutate: mutateForums } = useForums()

  const onSubmit = async data => {
    // console.log(data)
    setError(null)
    setLoading(true)
    try {
      await createForum(data)
      resetForm()
      mutateForums()
    } catch (err) {
      setError(err)
    } finally {
      setLoading(false)
    }
  }

  const categoriesTags = categories?.map(category => 
    <option value={category.id} key={category.id}>{category.name}</option>
  )

  return (
    <>
      <h1 className="title">Создать форум</h1>
      <form onSubmit={handleSubmit(onSubmit)}>
        <InputField label="ID"
          name="id"
          register={register}
          errors={errors} />
        <InputField label="Название"
          name="name"
          register={register}
          errors={errors} />
        <SelectField label="Категория"
                     name="categoryId"
                     register={register}
                     errors={errors}
                     defaultValue={-1}
                     isLoading={categoriesAreLoading}>
          {categories && categoriesTags}
        </SelectField>
        <SubmitField text="Создать" isLoading={isLoading} />
        {error && (
          <p className="help is-danger">{error.message}</p>
        )}
      </form>
    </>
  )
}