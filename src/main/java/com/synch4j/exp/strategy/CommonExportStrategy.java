package com.synch4j.exp.strategy;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.synch4j.exception.CallbackException;
import com.synch4j.exp.AbsExportZipStrategy;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ExportMode;
@Component
@Scope("prototype")
public class CommonExportStrategy extends AbsExportZipStrategy{

	@Override
	public ExportMode setExportMode() {
		return ExportMode.COMMON_EXPORT;
	}

	@Override
	public void prepareExportCallback(List<SynchPO> list)
			throws CallbackException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endExportCallback(String zipPath) throws CallbackException {
		// TODO Auto-generated method stub
		
	}

}
