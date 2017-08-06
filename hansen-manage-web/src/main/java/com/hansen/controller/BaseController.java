package com.hansen.controller;

import com.base.page.JsonResult;
import com.base.page.Page;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseController<D extends Object> {

    protected abstract JsonResult index(HttpServletRequest request, D model, Page page) throws Exception;

    protected abstract JsonResult add(HttpServletRequest request, D model) throws Exception;

    protected abstract JsonResult edit(HttpServletRequest request, D model) throws Exception;

    protected abstract JsonResult list(HttpServletRequest request, D model, Page page) throws Exception;

}
