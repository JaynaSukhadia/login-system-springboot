package com.aurosoft.loginsystem.controller;



import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aurosoft.loginsystem.entity.User;
import com.aurosoft.loginsystem.service.UserService;
import com.aurosoft.loginsystem.util.Helper;

import jakarta.servlet.http.HttpSession;



//import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/login")
public class ViewController {
	

	UserService userService;
	
public ViewController(UserService userService) {
		super();
		this.userService = userService;
	}

@GetMapping("/login")
	public String login() {
		
		return "login";
	}

@PostMapping("/login")
public String logincheck(@RequestParam("email") String email,
		@RequestParam("password")String password, HttpSession session) 
{
	User user = userService.findByEmailAndPassword(email, password);
	if(user!=null)
	{
		session.setAttribute("uname", user.getFname()+ "    "+user.getLname());
		session.setAttribute("fname", user.getFname());
		session.setAttribute("uid", user.getId());
		session.setAttribute("urole", user.getRole());
//		System.out.println("XXXXXXXXXXXXXXXX");
//		System.out.println(user);
//		System.out.println("XXXXXXXXXXXXXXXX");

		
		if(Helper.checkUserRole())
		{
			session.setAttribute("msg", "You are succesfully login..");
			return "redirect:/profile";
		}
		else
	{
		session.setAttribute("msg","something went wrong");
		return "redirect:/logout";
	}
	}
		else
		{
			session.setAttribute("msg", "Wrong Username or password");
			return "redirect:/login";
		}
	}	

	@GetMapping("/profile")
	public String profile(Model m,HttpSession session)
	{
		
		if(!Helper.checkUserRole())
		{
			return "redirect:/login";
		}
//		ServletRequestAttributes attr=(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//		HttpSession session = attr.getRequest().getSession();
		int uid = 0;
		if(session.getAttribute("uid") != null)
		{
			uid=(int)session.getAttribute("uid");
			
		}
	
		User user=userService.getUserById(uid);
		m.addAttribute("user", user);
		//session.removeAttribute("msg");
		
		return "user/profile";
	}
	
	@GetMapping(value="/editprofile")
	public String editprofile(Model m)
	{
		
		if(!Helper.checkUserRole())
		{
			return "redirect:/login";
		}
		ServletRequestAttributes attr=(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession();
		int uid = 0;
		if(session.getAttribute("uid") != null)
		{
			uid=(int)session.getAttribute("uid");
			
		}

		User user=userService.getUserById(uid);
		m.addAttribute("user", user);

		return "/user/edit_profile";
	}

	@PostMapping(value="/updateprofile")
	public String updateProfile(@ModelAttribute("user") User user,HttpSession session)
	{
		//user.setReg_date(new Date());
		
		User user2 =userService.getUserById(user.getId());
		user2.setFname(user.getFname());
		user2.setLname(user.getLname());
		user2.setEmail(user.getEmail());
		user2.setPhone(user.getPhone());
		
		userService.updateUser(user2);
		session.setAttribute("msg", "Your profile edited...");
		return "redirect:/profile";
	}

	
@GetMapping("/index")
public String index() {
	
	return "index";
}
@GetMapping("/signup")
public String signup(User user) {
	
	return "signup";
	}

@PostMapping("/newsignup")


public String newsignup(@ModelAttribute("user") User user,HttpSession session)
{
	user.setReg_date(new Date());
	userService.insertUser(user);
	
	session.setAttribute("msg", "Signup Successfully, you can login.....");
	return "redirect:/login";
}

@GetMapping("/forgot_pass")
public String forgot_pass(User user) {
	
	return "forgot_pass";
	}

@PostMapping("/update_forgot_pass")
public String forgotpass(@RequestParam("email") String email,
		 HttpSession session) 
{
	User user = userService.findByEmail(email);
if(user !=null)
{
	session.setAttribute("email", email);
	return "redirect:/reset_pass";
}else
{
		session.setAttribute("msg","something went wrong");
		return "redirect:/forgot_pass";
	}
}
	

@GetMapping("/reset_pass")
public String reset() {
	
	return "reset_pass";
}

@PostMapping("/reset_pass1")
public String resetpass(@RequestParam("password") String password,
		@RequestParam("cpassword") String cpassword, HttpSession session)
{
	if(password.equals(cpassword))
	{
		String email = session.getAttribute("email").toString();
		User user = userService.findByEmail(email);
		user.setPassword(password);
		user = userService.updateUser(user);
		if(user!=null)
		{
			session.setAttribute("msg","Password Reset");
			return "redirect:/login";
		}
		else
		{
			session.setAttribute("msg", "Something went wrong!!");
			return "redirect:/reset_pass";
		}
	}
		else
		{
			session.setAttribute("msg", "Password and confirm password not match");
			return "redirect:/reset_pass";
		}
	}



//@GetMapping("/profile")
//public String profile() {
//	
//	return "/user/profile";
//	}
@GetMapping("/change_pass")
public String change() {
	
	return "/user/change_pass";
	}


@PostMapping("/update_change_pass")
public String updatechangepass(@RequestParam("oldpassword")String oldpassword,@RequestParam("password") String password,
		@RequestParam("cpassword") String cpassword, HttpSession session)
{
	
	String email = session.getAttribute("email").toString();
	User user = userService.findByEmail(email);
	
	if(!user.getPassword().equals(oldpassword))
	{
		session.setAttribute("msg","old pass not match");
		return "redirect:/change_pass";
	}
	if(password.equals(cpassword))
	{
		
		user.setPassword(password);
		user = userService.updateUser(user);
		if(user!=null)
		{
			session.setAttribute("msg","Password Reset");
			return "redirect:/login";
		}
		else
		{
			session.setAttribute("msg", "Something went wrong!!");
			return "redirect:/reset_pass";
		}
	}
		else
		{
			session.setAttribute("msg", "Password and confirm password not match");
			return "redirect:/reset_pass";
		}
	}





@GetMapping("/logout")
public String logout() {
	ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession();
	
	if(session.getAttribute("uname") != null)
	 session.removeAttribute("uname");

	if(session.getAttribute("uid") != null)
		 session.removeAttribute("uid");
		
	if(session.getAttribute("urole") != null)
		 session.removeAttribute("urole");
	
	
	session.setAttribute("msg", "You are successfully logout from system");
			return "redirect:/index";
	}
}
