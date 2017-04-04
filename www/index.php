<?php

require('core/core.php');

  if(isset($_GET['action'])){
    if(file_exists('core/controllers/' . strtolower($_GET['action']) . 'Controller.php')){
      include('core/controllers/' . strtolower($_GET['action']) . 'Controller.php');
    }else{
      include('core/controllers/errorController.php');
    }
  }else{
    include('core/controllers/indexController.php');
  }

 ?>