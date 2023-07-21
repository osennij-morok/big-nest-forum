import { DateTime } from "luxon"

export default function Post({ post, role, onClickDelete }) {

  const publishDatetime = DateTime.fromSeconds(post.publishTimestamp, { locale: 'ru' })
  let publishDatetimeStr
  if (post.publishTimestamp <= 0) {
    publishDatetimeStr = ''
  } else {
    publishDatetimeStr = publishDatetime.toFormat("dd/LL/yy ccc HH:mm:ss")
  }
  // const placeholderDatetime = 'Аноним 09/05/23 Втр 11:47:21'

  const senderUsername = post.sender?.username ?? 'Аноним'

  return <>
    <div className="columns" style={{ marginBottom: '1em' }}>
      <div className="column box" style={{ padding: '8px', }}>
        <article className="media">
          <div className="media-content">
            <div className="content">
              <div>
                <span className="has-text-grey">
                  <span className="is-size-6">
                    <span>{post.topic}</span>
                    <span> {senderUsername} {publishDatetimeStr} </span>
                    #{post.id}
                  </span>
                </span>
                <p className="is-size-5" style={{ padding: '16px' }}>
                  {post.text}
                </p>
              </div>
            </div>
          </div>
          {(role === 'ADMIN' || role === 'MODER' || role === 'OWNER') && (
            <div className="media-right">
              <button className="delete" onClick={() => onClickDelete(post.id)}></button>
            </div>
          )}
        </article>
      </div>
    </div>
  </>
}

const samplePost = {
  id: 1,
  threadHeadPostId: 2,
  topic: '',
  text: 'Hello, world!',
}
