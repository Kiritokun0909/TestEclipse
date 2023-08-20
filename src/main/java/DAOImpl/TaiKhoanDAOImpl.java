package DAOImpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import DAO.TaiKhoanDAO;
import DAO.QuyenDAO;
import DAO.QuyenDAO.QuyenEnumID;
import entity.Quyen;
import entity.TaiKhoan;

@Transactional
public class TaiKhoanDAOImpl implements TaiKhoanDAO {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private QuyenDAO quyenDAO;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public int soLuongTaiKhoan() {
		long num = 0;
		Session session = sessionFactory.getCurrentSession();
		String hql = "Select COUNT(*) From TaiKhoan";
		Query query = session.createQuery(hql);
		try {
			num =  (long) query.uniqueResult();
		} catch (Exception e) {
			System.out.println(e);
		}
		return (int) num;
	}
	
	@Override
	public boolean themTaiKhoan(TaiKhoan acc) {
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();
		try {
			acc.setMaTaiKhoan(soLuongTaiKhoan() + 1);
			session.save(acc);
			t.commit();
			return true;
		} catch (Exception ex) {
			System.out.println(ex);
			return false;
		}
		finally {
			session.close();
		}
	}
	
	@Override
	public boolean suaTaiKhoan(TaiKhoan acc) {
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();
		try {
			session.update(acc);
			t.commit();
			return true;
		} catch (Exception ex) {
			System.out.println(ex);
			return false;
		}
		finally {
			session.close();
		}
	}
	
	@Override
	public boolean xoaTaiKhoan(TaiKhoan acc) {
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();
		try {
			acc.setTrangThai(false);
			session.update(acc);
			t.commit();
			return true;
		} catch (Exception ex) {
			System.out.println(ex);
			return false;
		}
		finally {
			session.close();
		}
	}

	@Override
	public TaiKhoan timTaiKhoan(String email) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "From TaiKhoan Where email = :username";
		Query query = session.createQuery(hql);
		query.setString("username", email);
		TaiKhoan acc = null;
		try {
			acc = (TaiKhoan) query.uniqueResult();
		} catch (Exception e) {
			System.out.println(e);
		}

		return acc;
	}
	
	@SuppressWarnings("unchecked")
	public List<entity.TaiKhoan> getListTaiKhoanViaRole(QuyenEnumID id){
		Session session = sessionFactory.getCurrentSession();
		
		Quyen quyen = quyenDAO.getQuyen(id);
		String hql = "from TaiKhoan Where maQuyen = :maQuyen";
		Query query = session.createQuery(hql);
		query.setParameter("maQuyen", quyen.getMaQuyen());
		
		return (List<TaiKhoan>)query.list();
		
	}

	@Override
	public TaiKhoan getAccount(int id) {
		Session session = sessionFactory.getCurrentSession();
		
		String hql = "from TaiKhoan Where maTaiKhoan = :id";
		Query query = session.createQuery(hql);
		query.setParameter("maTaiKhoan", id);
		
		TaiKhoan tk = null;
		try {
			tk = (TaiKhoan) query.uniqueResult();
		} catch (Exception ex) {
			
		}
		
		return tk;
	}
	
}
