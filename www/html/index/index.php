<?php include('html/overall/header.php') ?>
	<div class="container">
		<section class="main row">
			<article class="col-xs-12 col-sm-6 col-md-9 col-lg-9 table-responsive">
				<p>
					<?php  
						$conn = mysqli_connect('localhost','root','','mcm');
						if(!empty($_GET['tam'])){ 
							if ($_GET['tam'] == '100' OR $_GET['tam'] == '200' OR $_GET['tam'] == '500' OR $_GET['tam'] == '1000') {
								$sql = "SELECT * FROM registro ORDER BY id DESC LIMIT " . $_GET['tam'];
							}else if($_GET['tam'] == 'all'){
								$sql = "SELECT * FROM registro ORDER BY id DESC";
							}
						}else{
							$sql = "SELECT * FROM registro ORDER BY id DESC";
						}
						$result = mysqli_query($conn,$sql);
						echo "
							<table class='table table-striped table-hover'>
								<tr>
									<th>ID</th>
									<th>IdSensor</th>
									<th>Fecha</th> 
									<th>Humedad</th>
									<th>Presión</th>
									<th>Temperatura</th>
									<th>Luminosidad</th>
								</tr>";
						while($row = mysqli_fetch_array($result)){
							echo "
								<tr>
									<td>".$row[0]."</td>
									<td>".$row[1]."</td>
									<td>".$row[2]."</td>
									<td>".$row[3]."</td>
									<td>".$row[4]."</td>
									<td>".$row[5]."</td>
									<td>".$row[6]."</td>
								</tr>";
						}
						echo "</table>";
					?>
				</p>
			</article>

			<?php include('html/overall/aside.php') ?>
			
		</section>	
		<!--div class="row">
			<div class="color1 col-md-3">
				Lorem ipsum dolor sit amet, consectetur adipisicing elit. Magni dolore voluptatem ipsam. Maxime possimus, temporibus. Maxime perspiciatis ut totam aperiam laboriosam impedit optio dolores, esse sit minus eos, nihil modi.
			</div>
			<div class="col-md-3">
				Lorem ipsum dolor sit amet, consectetur adipisicing elit. Alias itaque aspernatur suscipit saepe voluptatem voluptatibus. Earum voluptatibus est ipsum repellendus iste quo, esse magnam aliquam unde libero porro, quidem tempore!
			</div>
			<div class="color1 col-md-3">
				Lorem ipsum dolor sit amet, consectetur adipisicing elit. Nam totam fugit, fugiat veniam rerum provident vel assumenda minima inventore voluptatum! Vitae maxime velit quis commodi architecto eligendi doloribus dolorem temporibus.
			</div>
			<div class="col-md-3">
				Lorem ipsum dolor sit amet, consectetur adipisicing elit. Officiis cum quaerat quasi ullam aspernatur ipsa sed delectus totam eum voluptatum illum quam doloremque, odit obcaecati asperiores recusandae modi iusto! Nemo.
			</div>
		</div-->
	</div>
	<?php include('html/overall/footer.php') ?>