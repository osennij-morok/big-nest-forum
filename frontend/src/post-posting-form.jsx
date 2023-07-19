import { useState } from "react";
import { createPost, usePosts } from './api/post'
import * as yup from "yup"
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { InputField } from "./forms/input-field";
import TextareaField from "./forms/textarea-field";
import SubmitField from "./forms/submit-field";

export default function CreatePostForm({ forumId, headPostId, fromId, onPostSubmitted }) {
  
  const postSchema = yup.object({
    topic: yup.string()
      .trim(),
    text: yup.string()
      .trim()
      .required("Требуется ввести текст"),
  })

  const {
    register,
    formState: { errors },
    reset: resetForm,
    handleSubmit
  } = useForm({
    resolver: yupResolver(postSchema)
  })
  
  const [isLoading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  // const { mutate: mutatePosts } = usePosts(forumId, headPostId, fromId)

  const newPostClicked = async postFormData => {
    setError(null)
    setLoading(true)
    const newPost = { ...postFormData, threadHeadPostId: headPostId }
    try {
      await createPost(newPost, forumId)
    } catch (err) {
      setError(err)
      return
    } finally {
      setLoading(false)
    }
    // mutatePosts()
    resetForm()
    onPostSubmitted()
  }

  return <>
    <form onSubmit={handleSubmit(newPostClicked)}>
      <InputField label="Тема"
                  name="topic"
                  register={register}
                  errors={errors} />
      <TextareaField label="Текст"
                     name="text"
                     cols="30"
                     rows="10"
                     register={register}
                     errors={errors}
                     handleSubmit={handleSubmit(newPostClicked)} />
      <SubmitField text="Отправить" isLoading={isLoading} />
      {error && (
        <p className="help is-danger">{error.message}</p>
      )}
    </form>
  </>
}