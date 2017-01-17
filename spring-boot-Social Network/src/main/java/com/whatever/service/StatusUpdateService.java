package com.whatever.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.whatever.model.StatusUpdate;
import com.whatever.model.Dao.StatusUpdateDao;

@Service
public class StatusUpdateService {
	
	
	private final static int PAGESIZE = 10;
	
	@Autowired
	private StatusUpdateDao statusUpdateDao;
	
	public void save(StatusUpdate statusUpdate){
		statusUpdateDao.save(statusUpdate);
	}
	
	public StatusUpdate getLatest(){
		return statusUpdateDao.findFirstByOrderByAddedDesc();
	}
	
	public Page<StatusUpdate> getPage(int pageNumber){	
		PageRequest request = new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.DESC, "added");
		
		return statusUpdateDao.findAll(request);
	}

	public void delete(Long id) {
		statusUpdateDao.delete(id);
	}

	public StatusUpdate get(Long id) {
		return statusUpdateDao.findOne(id);
	}

}
