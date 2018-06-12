package com.game.core.common.svn;

import java.io.File;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.game.core.common.logger.LoggerExecuteHandler;

/**
 * svn工具
 * @author wangzhiyuan
 *
 */
public class SVNUtils {
	
	public static void setupLibrary() {
		DAVRepositoryFactory.setup();
		SVNRepositoryFactoryImpl.setup();
		
		
	}
	
	/**
	 * 验证登录SVN
	 * @param svnRoot
	 * @param userName
	 * @param password
	 * @return
	 */
	public static SVNClientManager authSvn(String svnRoot,String userName,String password) {
		setupLibrary();
		SVNRepository repository=null;
		try {
			repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnRoot));
		} catch (SVNException e) {
			LoggerExecuteHandler.getInstance().dealExceptionLogger("svn init error", e);
		}
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, password.toCharArray());
		repository.setAuthenticationManager(authManager);
		DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
		SVNClientManager clientManager = SVNClientManager.newInstance(options, authManager);
		return clientManager;
	}
	
	/**
	 * 创建SVN目录
	 * @param clientManager
	 * @param url
	 * @param commitMessage
	 * @return
	 */
	public static SVNCommitInfo makeDirectory(SVNClientManager clientManager,SVNURL url,String commitMessage) {
		try {
			return clientManager.getCommitClient().doMkDir(new SVNURL[] {url}, commitMessage);
		} catch (Exception e) {
			LoggerExecuteHandler.getInstance().dealExceptionLogger("svn create dir error", e);
		}
		return null;
	}
	
	/**
	 * 导入文件夹
	 * @param clientManager
	 * @param localPath
	 * @param dstURL
	 * @param commitMessage
	 * @param isRecursive
	 * @return
	 */
	public static SVNCommitInfo importDirectory(SVNClientManager clientManager,File localPath,SVNURL dstURL,String commitMessage,boolean isRecursive) {
		try {
			return clientManager.getCommitClient().doImport(localPath, dstURL, commitMessage,null,true,true, SVNDepth.fromRecurse(isRecursive));
		} catch (Exception e) {
			LoggerExecuteHandler.getInstance().dealExceptionLogger("svn import dir error", e);
		}
		return null;
	}
	
	
    /**
     * 添加入口
     */
    public static void addEntry(SVNClientManager clientManager, File wcPath) {
        try {
            clientManager.getWCClient().doAdd(new File[]{wcPath}, true, false, false, SVNDepth.INFINITY, false, false, true);
        } catch (SVNException e) {
            LoggerExecuteHandler.getInstance().dealExceptionLogger("svn add entry error", e);
        }
    }

    /**
     * 显示状态
     */
    public static SVNStatus showStatus(SVNClientManager clientManager, File wcPath, boolean remote) {
        SVNStatus status = null;
        try {
            status = clientManager.getStatusClient().doStatus(wcPath, remote);
        } catch (SVNException e) {
        	LoggerExecuteHandler.getInstance().dealExceptionLogger("svn show status error", e);
        }
        return status;
    }

    /**
     * 提交
     * @Param keepLocks:是否保持锁定
     */
    public static SVNCommitInfo commit(SVNClientManager clientManager, File wcPath, boolean keepLocks, String commitMessage) {
        try {
            return clientManager.getCommitClient().doCommit(new File[]{wcPath}, keepLocks, commitMessage, null, null, false, false, SVNDepth.INFINITY);
        } catch (SVNException e) {
        	LoggerExecuteHandler.getInstance().dealExceptionLogger("svn commit error", e);
        }
        return null;
    }

    /**
     * 更新
     */
    public static long update(SVNClientManager clientManager, File wcPath, SVNRevision updateToRevision, SVNDepth depth) {
        SVNUpdateClient updateClient = clientManager.getUpdateClient();
        updateClient.setIgnoreExternals(false);
        try {
            return updateClient.doUpdate(wcPath, updateToRevision, depth, false, false);
        } catch (SVNException e) {
            LoggerExecuteHandler.getInstance().dealExceptionLogger("svn update error", e);
        }
        return 0;
    }

    /**
     * 从SVN导出项目到本地
     * @Param url:SVN的url
     * @Param revision:版本
     * @Param destPath:目标路径
     */
    public static long checkout(SVNClientManager clientManager, SVNURL url, SVNRevision revision, File destPath, SVNDepth depth) {
        SVNUpdateClient updateClient = clientManager.getUpdateClient();
        updateClient.setIgnoreExternals(false);
        try {
            return updateClient.doCheckout(url, destPath, revision, revision, depth, false);
        } catch(SVNException e) {
        	LoggerExecuteHandler.getInstance().dealExceptionLogger("svn checkout error", e);
        }
        return 0;
    }

    /**
     * 确定path是否是一个工作空间
     */
    public static boolean isWorkingCopy(File path) {
        if(!path.exists()) {
            LoggerExecuteHandler.getInstance().dealWarnLogger("'" + path + "' not exist!");
            return false;
        }
        try {
            if(null == SVNWCUtil.getWorkingCopyRoot(path, false)) {
                return false;
            }
        } catch (SVNException e) {
        	LoggerExecuteHandler.getInstance().dealExceptionLogger("svn check workingCopy error", e);
        }
        return true;
    }

    /**
     * 确定一个URL在SVN上是否存在
     */
    public static boolean isURLExist(SVNURL url, String username, String password) {
        try {
            SVNRepository svnRepository = SVNRepositoryFactory.create(url);
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password.toCharArray());
            svnRepository.setAuthenticationManager(authManager);
            SVNNodeKind nodeKind = svnRepository.checkPath("", -1); //遍历SVN,获取结点。
            return nodeKind == SVNNodeKind.NONE ? false : true;
        } catch (SVNException e) {
        	LoggerExecuteHandler.getInstance().dealExceptionLogger("svn check url exist error", e);
        }
        return false;
    }
	
	
}
