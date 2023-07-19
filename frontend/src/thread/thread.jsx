import { Link } from "react-router-dom";

export default function Thread({ thread, role, onClickDelete }) {
  const threadLink = <Link to={`/${thread.forumId}/${thread.id}`}> Ответить</Link>

  return <div className="columns is-centered"
    style={{ marginBottom: '2em', width: '100%' }}>
    {/* <div className="column box is-narrow">
            <h2>#{thread.id}</h2>
            <h2>Forum: /{thread.forumId}/</h2>
            <h2>Topic: {thread.topic}</h2>
            <p>{thread.text}</p>
        </div> */}

    <div className="column box">
      <article className="media">
        <div className="media-content">
          <div className="content">
            <p>
              <strong>{thread.topic} #{thread.id}</strong>
              {threadLink}
              <br />
              {thread.text}
            </p>
          </div>
        </div>
        {(role === 'ADMIN') && (
          <div className="media-right">
            <button className="delete" onClick={() => onClickDelete(thread.id)}></button>
          </div>
        )}
      </article>
    </div>
  </div>
}