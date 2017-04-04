<aside class="col-xs-12 col-sm-6 col-md-3 col-lg-3">
	<h4>
		Opciones
	</h4>
	<center>
		<a href="?action=index&tam=all" class="btn btn-warning btn-block <?php if(empty($_GET['tam']) OR $_GET['tam'] == 'all') echo 'disabled'?>" role="button">Todo</a>
		<a href="?action=index&tam=100" class="btn btn-warning btn-block <?php if(!empty($_GET['tam']) AND $_GET['tam'] == '100') echo 'disabled'?>" role="button">100</a>
		<a href="?action=index&tam=200" class="btn btn-warning btn-block <?php if(!empty($_GET['tam']) AND $_GET['tam'] == '200') echo 'disabled'?>" role="button">200</a>
		<a href="?action=index&tam=500" class="btn btn-warning btn-block <?php if(!empty($_GET['tam']) AND $_GET['tam'] == '500') echo 'disabled'?>" role="button">500</a>
		<a href="?action=index&tam=1000" class="btn btn-warning btn-block <?php if(!empty($_GET['tam']) AND $_GET['tam'] == '1000') echo 'disabled'?>" role="button">1000</a></br>


	</center>
</aside>