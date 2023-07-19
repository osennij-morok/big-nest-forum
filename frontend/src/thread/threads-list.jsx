import { useParams } from "react-router-dom"
import classNames from "classnames"
import Loading from "../loading"
import Thread from './thread'
import { createThread, useThreads } from "../api/thread"
import { useForum } from "../api/forum"
import { useState } from "react"
import * as yup from "yup"
import { useForm } from "react-hook-form"
import { yupResolver } from "@hookform/resolvers/yup"
import { InputField } from "../forms/input-field"
import SubmitField from "../forms/submit-field"
import { useAuthStore } from "../store/authentication"
import { deletePost } from "../api/post"

export default function Threads() {

  const { forumId } = useParams()
  const { forum, forumIsLoading } = useForum(forumId)
  const { 
    threads, 
    isLoading: threadsAreLoading, 
    error, 
    mutate: mutateThreads 
  } = useThreads(forumId)
  const userRole = useAuthStore(store => store.role)

  const onClickDeleteThread = async threadId => {
    try {
      await deletePost(forumId, threadId)
      mutateThreads()
    } catch (err) {
      console.error(err)
    }
  }

  const threadsTags = threads?.map(thread => 
    <Thread thread={thread} 
            key={thread.id} 
            role={userRole} 
            onClickDelete={onClickDeleteThread} />
  )

  const loadingTags = <Loading />

  const error500 = error?.response?.status == 500

  const errorText = error500
    ? `Forum ${forumId} does not exist`
    : undefined

  if (error) {
    console.error(error)
  }

  const errorColumnClasses = classNames('column', 'box', {
    'is-narrow': errorText,
    'is-four-fifths': !errorText,
  })

  const errorTags = <div className="columns">
    <div className="column">
      <div className="columns is-centered">
        <div className={errorColumnClasses}>
          <p>
            {errorText && errorText}
            {!errorText && JSON.stringify(error, null, 2)}
          </p>
        </div>
      </div>
    </div>
  </div>

  const noThreadsMessage = <div className="columns is-centered">
    <div className="column is-narrow">
      <h1 className="is-size-3 has-text-grey-light">Форум пуст</h1>
    </div>
  </div>

  return <>
    <div className="columns is-centered">
      <div className="column is-four-fifths">
        {forum && <ForumHeading forum={forum} />}
        {(threadsAreLoading || forumIsLoading) && loadingTags}
        {(threads?.length === 0) && noThreadsMessage}
        {threadsTags && threadsTags}
        {error && errorTags}
      </div>
    </div>
  </>
}

function ForumHeading({ forum }) {
  const [formIsOpened, setFormOpened] = useState(false)
  const buttonClicked = () => {
    setFormOpened(!formIsOpened)
  }

  return <div className="columns is-centered">
    <div className="column is-narrow content has-text-centered">
      <h1 className="is-size-1">{forum.name}</h1>
      <CreateThreadButton clicked={buttonClicked} formIsOpened={formIsOpened} />
      {formIsOpened && <CreateThreadForm forumId={forum.id} />}
    </div>
  </div>
}

function CreateThreadButton({ clicked, formIsOpened }) {
  const buttonText = formIsOpened
    ? 'Закрыть форму постинга'
    : 'Создать тред'

  return <a onClick={clicked}>
    <p className="is-size-5" style={{ marginBottom: '1em' }}>{buttonText}</p>
  </a>
}

function CreateThreadForm({ forumId }) {

  const threadSchema = yup.object({
    topic: yup.string()
      .trim()
      .required("Требуется ввести тему"),
    text: yup.string()
      .trim()
      .required("Требуется ввести текст"),
  })

  const { register, formState: { errors }, handleSubmit, reset: resetForm } = useForm({
    resolver: yupResolver(threadSchema)
  })

  const [isLoading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const { mutate: mutateThreads } = useThreads(forumId)

  const newThreadClicked = async threadFormData => {
    setError(null)
    setLoading(true)
    try {
      await createThread(forumId, threadFormData)
      resetForm()
    } catch (err) {
      setError(err)
      return
    } finally {
      setLoading(false)
    }
    mutateThreads()
  }

  return <div className="columns is-centered has-text-justified">
    <div className="column is-narrow">
      <form onSubmit={handleSubmit(newThreadClicked)}>
        <InputField label="Тема"
          name="topic"
          register={register}
          errors={errors} />
        <InputField label="Текст"
          name="text"
          register={register}
          errors={errors} />
        <SubmitField text="Отправить" isLoading={isLoading} />
        {error && (
          <p className="help is-danger">{error.message}</p>
        )}
      </form>
    </div>
  </div>
}