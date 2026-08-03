package com.shuzijun.lc;

import com.shuzijun.lc.command.CodeCommand;
import com.shuzijun.lc.command.CommonCommand;
import com.shuzijun.lc.command.CookieCommand;
import com.shuzijun.lc.command.FavoriteCommand;
import com.shuzijun.lc.command.FindCommand;
import com.shuzijun.lc.command.LoginCommand;
import com.shuzijun.lc.command.NoteCommand;
import com.shuzijun.lc.command.QuestionCommand;
import com.shuzijun.lc.command.SessionCommand;
import com.shuzijun.lc.command.SolutionCommand;
import com.shuzijun.lc.command.SubmissionCommand;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.model.FavoriteResult;
import com.shuzijun.lc.model.NoteUpdateResult;
import com.shuzijun.lc.model.PageInfo;
import com.shuzijun.lc.model.ProblemSetParam;
import com.shuzijun.lc.model.Question;
import com.shuzijun.lc.model.QuestionView;
import com.shuzijun.lc.model.RunCodeCheckResult;
import com.shuzijun.lc.model.RunCodeParam;
import com.shuzijun.lc.model.RunCodeResult;
import com.shuzijun.lc.model.Session;
import com.shuzijun.lc.model.Solution;
import com.shuzijun.lc.model.Submission;
import com.shuzijun.lc.model.SubmissionDetail;
import com.shuzijun.lc.model.SubmitCheckResult;
import com.shuzijun.lc.model.SubmitParam;
import com.shuzijun.lc.model.SubmitResult;
import com.shuzijun.lc.model.Tag;
import com.shuzijun.lc.model.User;

import java.util.List;

public final class LcApi {

    private final LcClient client;

    LcApi(LcClient client) {
        this.client = client;
    }

    public Questions questions() {
        return new Questions();
    }

    public Account account() {
        return new Account();
    }

    public Code code() {
        return new Code();
    }

    public Notes notes() {
        return new Notes();
    }

    public Favorites favorites() {
        return new Favorites();
    }

    public Sessions sessions() {
        return new Sessions();
    }

    public Solutions solutions() {
        return new Solutions();
    }

    public Submissions submissions() {
        return new Submissions();
    }

    public final class Questions {
        public PageInfo<QuestionView> list(ProblemSetParam param, RequestContext context) throws LcException {
            return client.invoker(QuestionCommand.buildProblemSetQuestionList(param, context(context)));
        }

        public List<QuestionView> all(RequestContext context) throws LcException {
            return client.invoker(QuestionCommand.buildAllQuestions(context(context)));
        }

        public Question get(String titleSlug, RequestContext context) throws LcException {
            return client.invoker(QuestionCommand.buildGetQuestion(titleSlug, context(context)));
        }

        public Question today(RequestContext context) throws LcException {
            return client.invoker(QuestionCommand.buildQuestionOfToday(context(context)));
        }

        public String random(ProblemSetParam param, RequestContext context) throws LcException {
            return client.invoker(QuestionCommand.buildRandomQuestion(param, context(context)));
        }

        public List<Tag> tags(RequestContext context) throws LcException {
            return client.invoker(FindCommand.buildTags(context(context)));
        }

        public List<Tag> lists(RequestContext context) throws LcException {
            return client.invoker(FindCommand.buildLists(context(context)));
        }

        public List<Tag> categories(RequestContext context) throws LcException {
            return client.invoker(FindCommand.buildCategory(context(context)));
        }
    }

    public final class Account {
        public boolean verify(RequestContext context) throws LcException {
            return client.invoker(CommonCommand.buildVerify(context(context)));
        }

        public boolean isLoggedIn(RequestContext context) throws LcException {
            return client.invoker(CommonCommand.buildCheckLogin(context(context)));
        }

        public User user(RequestContext context) throws LcException {
            return client.invoker(CommonCommand.buildGetUser(context(context)));
        }

        public User user(UserQueryMode mode, RequestContext context) throws LcException {
            return client.invoker(CommonCommand.buildGetUser(
                    mode == null ? UserQueryMode.SITE_DEFAULT : mode,
                    context(context)
            ));
        }

        public LoginCommand.LoginResult login(
                String username, String password, String csrfToken, RequestContext context) throws LcException {
            return client.invoker(LoginCommand.buildLogin(username, password, csrfToken, context(context)));
        }

        public void setCookie(String cookie, RequestContext context) throws LcException {
            client.invoker(CookieCommand.buildSetCookie(cookie, context(context)));
        }

        public void logout(RequestContext context) throws LcException {
            client.invoker(CookieCommand.buildLogout(context(context)));
        }
    }

    public final class Code {
        public RunCodeResult run(RunCodeParam param, RequestContext context) throws LcException {
            return client.invoker(CodeCommand.buildRunCode(param, context(context)));
        }

        public RunCodeCheckResult runResult(String interpretId, RequestContext context) throws LcException {
            return client.invoker(CodeCommand.buildRunCodeCheck(interpretId, context(context)));
        }

        public SubmitResult submit(SubmitParam param, RequestContext context) throws LcException {
            return client.invoker(CodeCommand.buildSubmitCode(param, context(context)));
        }

        public SubmitCheckResult submitResult(Integer submissionId, RequestContext context) throws LcException {
            return client.invoker(CodeCommand.buildSubmitCheck(submissionId, context(context)));
        }

        public SubmitCheckResult submitResultById(String submissionId, RequestContext context) throws LcException {
            return client.invoker(CodeCommand.buildSubmitCheckById(submissionId, context(context)));
        }
    }

    public final class Notes {
        public String get(String titleSlug, RequestContext context) throws LcException {
            return client.invoker(NoteCommand.buildGetNote(titleSlug, context(context)));
        }

        public boolean update(String titleSlug, String content, RequestContext context) throws LcException {
            return client.invoker(NoteCommand.buildUpdateNote(titleSlug, content, context(context)));
        }

        public NoteUpdateResult updateResult(
                String titleSlug,
                String content,
                RequestContext context
        ) throws LcException {
            return client.invoker(
                    NoteCommand.buildUpdateNoteResult(titleSlug, content, context(context))
            );
        }
    }

    public final class Favorites {
        public FavoriteResult add(String favoriteIdHash, String questionId, RequestContext context) throws LcException {
            return client.invoker(FavoriteCommand.buildAddQuestionToFavorite(
                    favoriteIdHash, questionId, context(context)));
        }

        public FavoriteResult remove(String favoriteIdHash, String questionId, RequestContext context)
                throws LcException {
            return client.invoker(FavoriteCommand.buildRemoveQuestionFromFavorite(
                    favoriteIdHash, questionId, context(context)));
        }
    }

    public final class Sessions {
        public List<Session> list(String userSlug, RequestContext context) throws LcException {
            return client.invoker(SessionCommand.buildSessionListCommand(userSlug, context(context)));
        }

        public boolean switchTo(Integer sessionId, RequestContext context) throws LcException {
            return client.invoker(SessionCommand.buildSwitchSession(sessionId, context(context)));
        }
    }

    public final class Solutions {
        public List<Solution> list(String titleSlug, RequestContext context) throws LcException {
            return client.invoker(SolutionCommand.buildSolutionList(titleSlug, context(context)));
        }

        public String article(String articleSlug, RequestContext context) throws LcException {
            return client.invoker(SolutionCommand.buildSolutionArticle(articleSlug, context(context)));
        }
    }

    public final class Submissions {
        public List<Submission> list(String titleSlug, int offset, int limit, RequestContext context)
                throws LcException {
            return client.invoker(SubmissionCommand.buildSubmissionList(
                    titleSlug, offset, limit, context(context)));
        }

        public SubmissionDetail detail(String submissionId, RequestContext context) throws LcException {
            return client.invoker(SubmissionCommand.buildSubmissionDetail(submissionId, context(context)));
        }
    }

    private static RequestContext context(RequestContext context) throws LcException {
        RequestContext effectiveContext = context == null ? RequestContext.DEFAULT : context;
        effectiveContext.throwIfCancellationRequested();
        return effectiveContext;
    }
}
