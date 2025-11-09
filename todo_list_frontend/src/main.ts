import './style.css'
import type { AuthRequest, AuthResponse, ErrorResponse } from './apiContracts';
import type { TaskRequestDto,TaskResponseDto, TaskUpdateDto } from './apiContracts';


const API_BASE_URL = 'http://localhost:8080/api/v1';


// View Elements
const authView= document.getElementById('auth-view') as HTMLDivElement;
const taskView= document.getElementById('task-view') as HTMLDivElement;


// Auth Elements
const loginForm=document.getElementById('login-form') as HTMLFormElement;
const loginUsername=document.getElementById('login-username') as HTMLInputElement;
const loginPassword=document.getElementById('login-password') as HTMLInputElement;

const registerForm=document.getElementById('register-form') as HTMLFormElement;
const registerUsername=document.getElementById('register-username') as HTMLInputElement;
const registerPassword=document.getElementById('register-password') as HTMLInputElement;

const authError=document.getElementById('auth-error') as HTMLDivElement;
const logoutButton=document.getElementById('logout-button') as HTMLButtonElement;

//toggle switch elements
const authToggle = document.getElementById('auth-toggle-checkbox') as HTMLInputElement;
const loginLabel = document.getElementById('login-label') as HTMLSpanElement;
const registerLabel = document.getElementById('register-label') as HTMLSpanElement;
// Task Elements
const taskListContainer=document.getElementById('task-list-container') as HTMLDivElement;
const createTaskForm=document.getElementById('create-task-form') as HTMLFormElement;
const newTaskTitle=document.getElementById('task-title') as HTMLInputElement;
const newTaskDescription=document.getElementById('task-description') as HTMLInputElement;

const taskError=document.getElementById('task-error') as HTMLDivElement;




// login 

async function handleLogin(event: SubmitEvent){
  event.preventDefault();

  const loginData:AuthRequest={
    username: loginUsername.value,
    password: loginPassword.value
  };

  try{
    const response=await fetch(`${API_BASE_URL}/auth/login`,{
      method:'POST',
      headers:{
        'Content-Type':'application/json'
      },
      body:JSON.stringify(loginData)
    });

    if(!response.ok){
      const errorData:ErrorResponse=await response.json();  
      throw new Error(errorData.message|| 'Login failed');
    }

    
  const {token}:AuthResponse = await response.json();

  localStorage.setItem('jwt-token', token);
  showTaskView();
  }catch(error){
    console.error('Login error:', error);
    authError.textContent=(error as Error).message;
  }
}


// Registration 
async function handleRegister(event: SubmitEvent){
  event.preventDefault();

  const registerData:AuthRequest={
    username:registerUsername.value,
    password:registerPassword.value
  }

  try{
    const response=await fetch(`${API_BASE_URL}/auth/register`,{
      method:'POST',
      headers:{
        'Content-Type':'application/json'
      },
      body:JSON.stringify(registerData)
    });
    if(!response.ok){
      const errorData:ErrorResponse=await response.json();
      throw new Error(errorData.message || 'Registration failed');
    }
    alert('Registration successful! Please log in.');
    showAuthView();
  }catch(error){
    console.error('Registration error:', error);
    authError.textContent=(error as Error).message;
  }
}


// Task View and creation

async function fetchAndDisplayTasks(){
  const token=localStorage.getItem('jwt-token');
  if(!token){
    showAuthView();
    return;
  }

  try{
    const response=await fetch(`${API_BASE_URL}/task`,{
      method:'GET',
      headers:{
        'Content-Type':'application/json',
        'Authorization':`Bearer ${token}`
      }
    });

    if(!response.ok){
      const errorData:ErrorResponse=await response.json();
      throw new Error(errorData.message || 'Failed to fetch tasks');
    }

    const pageData =await response.json();
    const tasks:TaskResponseDto[]=pageData.content;

    taskListContainer.innerHTML='';
    taskError.innerHTML='';

    if(tasks.length===0){
      taskListContainer.innerHTML='<p>No tasks found. Create a new task!</p>';
      return;
    }
    tasks.forEach(task=>{
      const taskElement=document.createElement('div');
      taskElement.className='task-item';

      const taskHeader=document.createElement('div');
      taskHeader.className='task-header';

      const checkbox=document.createElement('input');
      checkbox.type='checkbox';
      checkbox.checked=task.isCompleted;
      checkbox.dataset.taskId=task.id.toString();
      checkbox.addEventListener('change',()=>handleToggleComplete(Number(task.id),checkbox.checked));

      const title=document.createElement('span');
      title.textContent=task.title;
      title.className='task-title';
      if(task.isCompleted){
        title.classList.add('completed');
      }

      const deleteButton=document.createElement('button');
      deleteButton.textContent='Delete';
      deleteButton.dataset.taskId=task.id.toString();
      deleteButton.addEventListener('click',()=>handleDeleteTask(Number(task.id)));

      const description=document.createElement('div');
      description.textContent=task.description;
      description.className='task-description';
      description.style.display='none';

      const createdAt=document.createElement('div');
      createdAt.textContent=task.createdAt;
      createdAt.className='task-createdAt';
      createdAt.style.display='none';
      
      title.addEventListener('click',()=>{
        const hidden=description.style.display==='none';
        description.style.display=hidden ?'block':'none';

        const hiddenCreatedAt=createdAt.style.display==='none';
        createdAt.style.display=hiddenCreatedAt ?'block':'none';

      })

      taskHeader.appendChild(checkbox);
      taskHeader.appendChild(title);
      taskHeader.appendChild(deleteButton);

      taskElement.appendChild(taskHeader);
      taskElement.appendChild(description);
      taskListContainer.appendChild(taskElement);
    })

  }catch(error){
    console.error('Fetch tasks error:', error);
    taskError.textContent=(error as Error).message;
  }
}

async function handleCreateTask(event:SubmitEvent){
  event.preventDefault();

  const token =localStorage.getItem('jwt-token');
  if(!token){
    showAuthView();
    return;
  }

  const taskData:TaskRequestDto={
    title:newTaskTitle.value,
    description:newTaskDescription.value
  }

  try{
    const response=await fetch(`${API_BASE_URL}/task`,{
      method:'POST',
      headers:{
        'Content-Type':'application/json',
        'Authorization':`Bearer ${token}`
      },
      body:JSON.stringify(taskData)
    });

    if(!response.ok){
      const errorData:ErrorResponse=await response.json();
      throw new Error(errorData.message || 'Failed to create task');
    }

    newTaskTitle.value='';
    newTaskDescription.value='';
    fetchAndDisplayTasks();
  }catch(error){
    console.error('Create task error:', error);
    taskError.textContent=(error as Error).message;
  }
}


async function handleDeleteTask(taskId:number){
  const token=localStorage.getItem('jwt-token');
  if(!token){
    showAuthView();
    return;
  }

  if (!confirm('Are you sure you want to delete this task?')) {
    return;
  }

  try{
    const response=await fetch(`${API_BASE_URL}/task/${taskId}`,{
      method:'DELETE',
      headers:{
        'Authorization':`Bearer ${token}`
      }
    });

    if(!response.ok){
      const errorData:ErrorResponse=await response.json();
      throw new Error(errorData.message || 'Failed to delete task');
    }

    fetchAndDisplayTasks();
  }catch(error){
    console.error('Delete task error:', error);
    taskError.textContent=(error as Error).message;
  }
}

async function handleToggleComplete(taskId:number,isCompleted:boolean){

  const token=localStorage.getItem('jwt-token');

  if(!token){
    showAuthView();
    return;
  }

  const updateData:TaskUpdateDto={
    isCompleted:isCompleted
  };

  try{
    const response = await fetch(`${API_BASE_URL}/task/${taskId}`,{
      method:'PATCH',
      headers:{
        'Content-Type':'application/json',
        'Authorization':`Bearer ${token}`
      },
      body:JSON.stringify(updateData)
    });

    if(!response.ok){
      const errorData:ErrorResponse=await response.json();
      throw new Error(errorData.message || 'Failed to update task');
    }

    fetchAndDisplayTasks();
  }catch(error){
    console.error('Update task error:', error);
    taskError.textContent=(error as Error).message;
  }
}

// helper funtions
function toggleAuthForm() {
    if (authToggle.checked) {
        // --- Show Register Form ---
        loginForm.style.display = 'none';
        registerForm.style.display = 'flex';
        loginLabel.classList.remove('active');
        registerLabel.classList.add('active');
    } else {
        // --- Show Login Form ---
        loginForm.style.display = 'flex';
        registerForm.style.display = 'none';
        loginLabel.classList.add('active');
        registerLabel.classList.remove('active');
    }
    authError.textContent = ''; // Clear errors on toggle
}
function showAuthView(){
  authView.style.display='block';
  taskView.style.display='none';
  authToggle.checked = false; 
    toggleAuthForm();
}

function showTaskView(){
  authView.style.display='none';
  taskView.style.display='block';
  fetchAndDisplayTasks();
}

function handleLogout(){
  localStorage.removeItem('jwt-token');
  showAuthView();
}

function checkInitialAuthState(){
  const token=localStorage.getItem('jwt-token');
  if(token){
    showTaskView();
  }else{
    showAuthView();
  }
}

authToggle.addEventListener('change', toggleAuthForm);

loginForm.addEventListener('submit',handleLogin);
registerForm.addEventListener('submit',handleRegister)
logoutButton.addEventListener('click',handleLogout);
createTaskForm.addEventListener('submit',handleCreateTask);


checkInitialAuthState();