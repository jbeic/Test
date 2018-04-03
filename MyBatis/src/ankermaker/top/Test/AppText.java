package ankermaker.top.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.jbeic.mybatis.UserBean;


public class AppText {


	@Test
	public void teste() {
		String resource = "conf.xml"; // 定位核心配置文件
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream); // 创建
			SqlSession sqlSession = sqlSessionFactory.openSession(); // 获取�?
			// 调用 mapper 中的方法：命名空�?+ id
			List<UserBean> personList = sqlSession.selectList("com.jbeic.mybatis.UserBeanmapper.findAll");
			for (UserBean p : personList) {
				System.out.println(p);
		        sqlSession.insert("com.jbeic.mybatis.UserBeanmapper.insert", p);
		        sqlSession.delete("com.jbeic.mybatis.UserBeanmapper.delete", p);
			}
			sqlSession.commit();
			personList.clear();
			personList = sqlSession.selectList("com.jbeic.mybatis.UserBeanmapper.findAll");
			for (UserBean p : personList) {
				p.setUsername("DDDDD");
		        sqlSession.update("com.jbeic.mybatis.UserBeanmapper.update", p);
			}
	        sqlSession.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
