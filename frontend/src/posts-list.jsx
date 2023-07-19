import classNames from "classnames"
import { useEffect } from "react"
import { useState } from "react"
import { useRef } from "react"
import { Link, useNavigate, useParams } from "react-router-dom"
import { deletePost, usePost, usePosts, usePostsPage } from "./api/post"
import Loading from "./loading"
import Post from "./post"
import CreatePostForm from "./post-posting-form"
import { useAuthStore } from "./store/authentication"

export default function Posts() {

  const { forumId, threadId: threadIdStr } = useParams()
  const threadId = Number(threadIdStr)
  const userRole = useAuthStore((store) => store.role)
  const onSessionExpired = useAuthStore(store => store.onSignedOut)

  const intersectorRef = useRef(null)

  const [startId, setStartId] = useState(0)
  // const [totalPosts, setTotalPosts] = useState([])
  const [intersectionsCount, setIntersectionsCount] = useState(0)
  // const [temp, setTemp] = useState(false)

  const {
    post: threadHeadPost,
    isLoading: threadHeadIsLoading,
    error: threadHeadError,
  } = usePost(forumId, threadId)

  if (threadHeadError) {
    console.error(threadHeadError)
  }

  const threadHeadTags = threadHeadPost
    ? <Post post={threadHeadPost} />
    : undefined

  // const {
  //   posts,
  //   isLoading: postsAreLoading,
  //   error: postsError,
  //   mutate: mutatePosts
  // } = usePosts(forumId, threadId, startId)

  const DEFAULT_POSTS_LIMIT = 20
  const {
    posts,
    isLoading: postsAreLoading,
    error: postsError,
    lastPostId,
    totalCount: postsTotalCount,
    setPostsLimit,
    updatePosts,
  } = usePostsPage(forumId, threadId, startId, DEFAULT_POSTS_LIMIT)

  const onDeleteClicked = async postId => {
    try {
      await deletePost(forumId, postId)
      updatePosts()
    } catch (err) {
      if (err.code == 'SESSION_EXPIRED') {
        onSessionExpired()
        return
      }
      throw err
    }
    // mutatePosts()
  }

  const observerRef = useRef(
    new IntersectionObserver((entries) => {
      const [entry] = entries
      if (entry.isIntersecting) {
        // setIntersectionsCount((prev) => {
        //   console.log(`Change intersection state. Prev: ${prev}`)
        //   return prev + 1
        // })
        console.log(`INTERSECTION #${intersectionsCount}`)
        // console.log(`Start from id: ${startId}`)
        console.log(`Start id: ${startId}`)
        console.log(`Last post id: ${lastPostId.current}`) ////
        setStartId(lastPostId.current)
      }
    })
  )

  const loadRemainingPosts = () => {
    const postsLimit = postsTotalCount - posts.length
    setPostsLimit(postsLimit)
    console.log(`Posts limit: ${postsLimit}`)
  }

  // useEffect(() => {
  //   setPostsLimit(DEFAULT_POSTS_LIMIT)
  // }, [posts])

  useEffect(() => {
    const observer = observerRef.current
    const intersector = intersectorRef.current
    if (intersector) {
      observer.observe(intersector)
    }
    return () => {
      if (intersector) {
        observer.unobserve(intersector)
      }
    }
  }, [])

  const postsTags = posts?.map(post => (
    <Post 
      post={post}
      key={post.id}
      role={userRole}
      onClickDelete={onDeleteClicked} />
  ))

  if (postsError) {
    console.error(postsError)
  }

  const errorTags = <div className="columns is-centered">
    <div className="column is-narrow">
      Возникла ошибка
    </div>
  </div>

  const noPostsMessage = <div className="columns is-centered" style={{ marginBottom: '2em' }}>
    <div className="column is-narrow box">
      В этой ветке нет постов
    </div>
  </div>

  const onPostSubmitted = async () => {
    await updatePosts()
  }

  const postingForm = <div className="columns is-centered">
    <div className="column is-narrow box">
      <CreatePostForm 
        forumId={forumId} 
        headPostId={threadId} 
        fromId={startId}
        onPostSubmitted={onPostSubmitted} />
    </div>
  </div>

  // const createPages = (len) => {
  //   const arr = []
  //   for (let i = 0; i < len; i++) {
  //     arr.push(i + 1)
  //   }
  //   return arr
  // }

  // const [currentPage, setCurrentPage] = useState(10)
  // const [pagesList, setPagesList] = useState(() => createPages(30))
  // const onPrevPage = () => {
  //   if (currentPage == 1) {
  //     return
  //   }
  //   setCurrentPage(currentPage - 1)
  // }
  // const onNextPage = () => {
  //   if (currentPage == pagesList.length) {
  //     return
  //   }
  //   setCurrentPage(currentPage + 1)
  // }

  const onClickGoDown = () => {
    loadRemainingPosts()
    intersectorRef.current.scrollIntoView({ behavior: 'smooth' })
    console.log(`Total count: ${postsTotalCount}`)
    console.log(`Loaded posts count: ${posts?.length}`)
  }

  return <>
    <a className="button is-info is-size-4 go-down-btn" onClick={onClickGoDown}>Вниз</a>
    <div className="columns is-centered">
      <div className="column is-three-fifths">
        {threadHeadPost && threadHeadTags}

        {posts && postsTags}
        <div ref={intersectorRef}></div>
        {(posts && posts.length == 0) && noPostsMessage}
        {(postsAreLoading || threadHeadIsLoading) && <Loading />}
        {postsError && errorTags}
        {threadHeadPost && postingForm}
      </div>
    </div>
  </>
}

function Pagination({ current, allPages, onPrev, onNext }) {

  const pagesCount = allPages.length

  const firstPage = allPages[0]
  const lastPage = allPages[allPages.length - 1]

  const currentIsFirst = current == firstPage
  const currentIsMiddle = pagesCount >= 3 && current > 1 && current < lastPage
  const currentIsLast = pagesCount > 1 && current == lastPage

  // const onPrev = () => {
  //   // useNavigate()
  // }

  // const onNext = () => {

  // }

  return (
    <>
      <nav className="pagination is-centered" role="navigation" aria-label="pagination">
        <a className="pagination-previous" onClick={onPrev}>Сюды</a>
        <a className="pagination-next" onClick={onNext}>Туды</a>
        <ul className="pagination-list">
          {currentIsFirst && (
            <>
              <PaginationLink number={current} isCurrent />

              {(pagesCount <= 5) && (
              // O + oooo
                allPages.slice(1)
                  .map(page => <PaginationLink number={page} key={page} />)
              )}

              {(pagesCount > 5) && (
              // O + o...oo
                <>
                  <PaginationLink number={current + 1} />
                  <PaginationEllipsis />
                  <PaginationLink number={lastPage - 1} />
                  <PaginationLink number={lastPage} />
                </>
              )}
            </>
          )}

          {currentIsMiddle && (
            <>
              {(current <= 5) && (
              // oooo + O [...]
                allPages.slice(0, current - 1) // current - 1 = currentPageIndex
                  .map(page => <PaginationLink number={page} key={page} />)
              )}

              {(current > 5) && (
              // oo...o + O [...]
                <>
                  <PaginationLink number={1} />
                  <PaginationLink number={2} />
                  <PaginationEllipsis />
                  <PaginationLink number={current - 1} />
                </>
              )}

              <PaginationLink number={current} isCurrent />

              {(lastPage - current <= 4) && (
              // [...] O + oooo
                allPages.slice(current) // current = currentPageIndex + 1
                  .map(page => <PaginationLink number={page} key={page} />)
              )}

              {(lastPage - current > 4) && (
              // [...] O + o...oo
                <>
                  <PaginationLink number={current + 1} />
                  <PaginationEllipsis />
                  <PaginationLink number={lastPage - 1} />
                  <PaginationLink number={lastPage} />
                </>
              )}
            </>
          )}

          {currentIsLast && (
            <>
              {(pagesCount <= 5) && (
              // oooo + O
                allPages.slice(0, lastPage - 2) // lastPage - 2 = lastPageIndex - 1
                  .map(page => <PaginationLink number={page} key={page} />)
              )}

              {(pagesCount > 5) && (
              // oo..o + O
                <>
                  <PaginationLink number={1} />
                  <PaginationLink number={2} />
                  <PaginationEllipsis />
                  <PaginationLink number={lastPage - 1} />
                </>
              )}

              <PaginationLink number={current} isCurrent />
            </>
          )}
        </ul>
      </nav>
    </>
  )
}

function PaginationLink({ number, isCurrent }) {

  const linkClasses = classNames('pagination-link', { 'is-current': isCurrent })

  return (
    <li><Link className={linkClasses} to={`${number}`}>{number}</Link></li>
  )
}

function PaginationEllipsis() {
  return (
    <li><span className="pagination-ellipsis">&hellip;</span></li>
  )
}