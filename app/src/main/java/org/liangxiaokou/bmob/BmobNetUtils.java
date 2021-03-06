package org.liangxiaokou.bmob;

import android.content.Context;

import org.liangxiaokou.app.MApplication;
import org.liangxiaokou.bean.Album;
import org.liangxiaokou.bean.Friend;
import org.liangxiaokou.bean.LoveDate;
import org.liangxiaokou.bean.User;
import org.liangxiaokou.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.StatisticQueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.ValueEventListener;

/**
 * Created by moziqi on 16-4-9.
 */
public class BmobNetUtils {


    /**
     * 注册用户
     *
     * @param context
     * @param email
     * @param password
     * @param bmobListener
     */
    public static void signUp(final Context context, final String email, final String password, final BmobListener bmobListener) {
        final User user = new User();
        user.setUsername(email);
        user.setPassword(password);
        user.setEmail(email);
        user.setNick("");//默认为空
        user.setSex(1);//默认设置为女
        user.setIsOk(false);//默认没完善信息
        user.setMood("我爱小俩口app");
        user.setLoveDateObjectId("-1");
        user.setHaveLove(false);
        //邮箱验证
        user.setEmailVerified(true);
        //注册用户
        user.signUp(context.getApplicationContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                //插入love记录
                final LoveDate loveDate = new LoveDate();
                loveDate.setLoveDate("");
                loveDate.save(context.getApplicationContext(), new SaveListener() {
                    @Override
                    public void onSuccess() {
                        //执行更新用户信息
                        User updateUser = new User();
                        updateUser.setObjectId(user.getObjectId());
                        updateUser.setUsername(email);
                        updateUser.setPassword(password);
                        updateUser.setEmail(email);
                        updateUser.setNick("");//默认为空
                        updateUser.setSex(1);//默认设置为女
                        updateUser.setIsOk(false);//默认没完善信息
                        updateUser.setMood("我爱小俩口app");
                        updateUser.setLoveDateObjectId(loveDate.getObjectId());
                        updateUser.setHaveLove(false);
                        BmobNetUtils.updateUserInfo(context, updateUser, user.getObjectId(), new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                bmobListener.onSuccess();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                bmobListener.onFailure(i, s);
                            }
                        });
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        bmobListener.onFailure(i, s);
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                bmobListener.onFailure(i, s);
            }
        });
    }

    /**
     * 登录
     *
     * @param context
     * @param username
     * @param password
     * @param saveListener
     */
    public static void login(Context context, String username, String password, SaveListener saveListener) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.login(context.getApplicationContext(), saveListener);
    }

    /**
     * 登录
     *
     * @param context
     * @param username
     * @param password
     * @param saveListener
     */
    public static void loginByAccount(Context context, String username, String password, LogInListener saveListener) {
        User.loginByAccount(context.getApplicationContext(), username, password, saveListener);
    }

    /**
     * 通过账号查询用户信息
     *
     * @param context
     * @param findListener
     */
    public static void findCurrentUserInfo(Context context, FindListener<User> findListener) {
        String username = "";
        User currentUser = User.getCurrentUser(context.getApplicationContext(), User.class);
        if (currentUser != null) {
            username = currentUser.getUsername();
        }
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("username", username);
        query.findObjects(context.getApplicationContext(), findListener);
    }


    public static void findUserInfoByUserId(Context context, String objectId, FindListener<User> findListener) {
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("objectId", objectId);
        query.findObjects(context.getApplicationContext(), findListener);
    }

    /**
     * 更新用户信息
     *
     * @param context
     * @param user
     * @param userId
     * @param updateListener
     */
    public static void updateUserInfo(Context context, User user, String userId, UpdateListener updateListener) {
        user.update(context, userId, updateListener);
    }

    /**
     * 查询是否已经有关联的好友
     *
     * @param context
     * @param friendSQLQueryListener
     */
    public static void queryHasFriend(Context context, SQLQueryListener<Friend> friendSQLQueryListener) {
        BmobQuery<Friend> friendBmobQuery = new BmobQuery<>();
        //判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
        boolean isCache = friendBmobQuery.hasCachedResult(context, Friend.class);
        if (isCache) {//--此为举个例子，并不一定按这种方式来设置缓存策略
            friendBmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        } else {
            friendBmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        friendBmobQuery.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        String currentUserId = "";
        User currentUser = User.getCurrentUser(context.getApplicationContext(), User.class);
        if (currentUser != null) {
            currentUserId = currentUser.getObjectId();
        }
        String bql = "select * from Friend where (currentUserId = ? or friendUserId= ?) and isLove = ?";
        friendBmobQuery.doSQLQuery(context.getApplicationContext(), bql, friendSQLQueryListener, currentUserId, currentUserId, 0);
    }

    /**
     * 保存好友
     *
     * @param context
     * @param friendUserId
     * @param saveListener
     */
    public static void saveFriendBatch(Context context, String friendUserId, String friendName, SaveListener saveListener) {
        //获取当前用户id
        String currentUserId = "";
        User currentUser = User.getCurrentUser(context.getApplicationContext(), User.class);
        if (currentUser != null) {
            currentUserId = currentUser.getObjectId();
        }
        List<BmobObject> friends = new ArrayList<>();
        //添加好友
        Friend currentFriend = new Friend();
        currentFriend.setIsLove(0);//设置为有爱人
        currentFriend.setCurrentUserId(currentUserId);
        currentFriend.setCurrentName(currentUser.getNick());
        currentFriend.setFriendUserId(friendUserId);
        currentFriend.setFriendName(friendName);
        friends.add(currentFriend);
        //添加好友
        Friend otherFriend = new Friend();
        otherFriend.setIsLove(0);//设置为有爱人
        otherFriend.setCurrentUserId(friendUserId);
        otherFriend.setCurrentName(friendName);
        otherFriend.setFriendUserId(currentUserId);
        otherFriend.setFriendName(currentUser.getNick());
        friends.add(otherFriend);
        //实现批处理
        new BmobObject().insertBatch(context.getApplicationContext(), friends, saveListener);
    }

    /**
     * 保存好友
     *
     * @param context
     * @param currentUserId
     * @param friendUserId
     * @param friendName
     * @param saveListener
     */
    public static void saveOneFriend(Context context, String currentUserId, String friendUserId, String friendName, SaveListener saveListener) {
        //添加好友
        Friend currentFriend = new Friend();
        currentFriend.setIsLove(0);//设置为有爱人
        currentFriend.setCurrentUserId(currentUserId);
        currentFriend.setFriendUserId(friendUserId);
        currentFriend.setFriendName(friendName);
        currentFriend.save(context.getApplicationContext(), saveListener);
    }


    /**
     * 更新恋爱日
     *
     * @param context
     * @param sloveDate
     * @param updateListener
     */
    public static void updateLove(Context context, String sloveDate, UpdateListener updateListener) {
        LoveDate loveDate = new LoveDate();
        loveDate.setLoveDate(sloveDate);
        loveDate.update(context.getApplicationContext(), User.getCurrentUser(context, User.class).getLoveDateObjectId(), updateListener);
    }


    /**
     * 查询恋爱日
     *
     * @param context
     * @param listener
     */
    public static void queryLove(Context context, GetListener<LoveDate> listener) {
        BmobQuery<LoveDate> query = new BmobQuery<LoveDate>();
        query.getObject(context.getApplicationContext(), User.getCurrentUser(context, User.class).getLoveDateObjectId(), listener);
    }


    /**
     * 查询日志
     * @param context
     * @param page
     * @param findListener
     */
    public static void queryAlbums(Context context, int page, FindListener findListener) {
        BmobQuery<Album> query = new BmobQuery<>();
        User currentUser = User.getCurrentUser(context.getApplicationContext(), User.class);
        query.addWhereEqualTo("loveDateObjectId", currentUser.getLoveDateObjectId());
        query.setLimit(50);
        if (page > 0) {
            query.setSkip(50 * page);
        }
        query.findObjects(context.getApplicationContext(), findListener);
    }


    /**
     * 批量上传文件
     *
     * @param context
     * @param filePaths
     * @param uploadBatchListener
     */
    public static void updateFileBatch(Context context, String[] filePaths, UploadBatchListener uploadBatchListener) {
        Bmob.uploadBatch(context, filePaths, uploadBatchListener);
    }
}
