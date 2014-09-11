package com.fgsecure.ujoolt.app.utillity;

public class Language {
	public static final boolean FRANCE = false;
	public static final boolean ENGLISH = true;

	public static String login;
	public static String connectFacebook;
	public static String connectTwitter;
	public static String register;
	public static String buttonConfirmRegisterName;
	public static String yes;
	public static String no;
	public static String cancel;
	public static String close;
	public static String locationOrJolt;
	public static String location;
	public static String push;
	public static String hintSeekbar;
	public static String setting;
	public static String languageLabel;
	public static String reportAProblem;
	public static String tutorial;
	public static String about;
	public static String pullToRejolt;
	public static String releaseToRejolt;
	public static String userName;
	public static String eMail;
	public static String password;
	public static String loading;
	public static String upLoading;
	public static String waiting;
	public static String shareWithFacebook;
	public static String shareWithTwitter;
	public static String logout;
	public static String pleaseWait;
	public static String typeYourTextHere;
	public static String searchLocationOrJolt;
	public static String warning;
	public static String notifyEmptyText;
	public static String confirm;
	public static String signUp;
	public static String tryAgain;
	public static String loginNotSuccess;
	public static String accountUjoolt;
	public static String alert;
	public static String filter;
	public static String myJolt;
	public static String recent;
	public static String favourite;
	public static String bodyMailReportProblem;
	public static String subjectMailReportProblem;

	// confirm
	public static String confirmDelete;
	public static String confirmUnfavourite;
	public static String confirmDeleteFavourite;
	public static String confirmLogout;
	public static String confirmTurnOnGPS;
	public static String confirmTurnOnNetwork;
	public static String confirmExit;
	public static String confirmLoginFacebook;

	// notify
	public static String notifyLogin;
	public static String notifyNoResult;
	public static String notifyNoNetwork;
	public static String notifyAuthenError;
	public static String notifyVideoMoreThan15s;
	public static String notifyNotEnoughInput;
	public static String notifyExistEmail;
	public static String notifyCheckEmail;
	public static String notifyPassNotMatch;
	public static String notifyEmailNotValid;
	public static String notifyMessage;
	public static String notifyDeleteSuccessful;
	public static String notifyTurnOnGPS;

	public static void setLanguage(boolean language) {
		if (language == FRANCE) {
			login = "Connexion";
			connectFacebook = "             Connect w/ Facebook";
			connectTwitter = "             Connect w/ Twitter";
			register = "Inscription";
			locationOrJolt = "Lieu ou Jolt";
			location = "Lieu";
			push = "Push";
			setting = "Mise";
			languageLabel = "English";
			reportAProblem = "Signaler un problème";
			tutorial = "Didacticiel";
			about = "À propos";
			pullToRejolt = "Tirer pour rejolter...";
			releaseToRejolt = "Lâcher pour rejolter...";
			userName = "Pseudo";
			eMail = "E-mail";
			password = "Mot de pass";
			loading = "Chargement...";
			upLoading = "Téléchargement...";
			waiting = "Attente...";
			shareWithFacebook = "Partager sur facebook";
			shareWithTwitter = "Partager sur twitter";
			logout = "Déconnexion";
			pleaseWait = "Attendez s\'il vous plaît...";
			typeYourTextHere = "Tapez votre texte ici...";
			searchLocationOrJolt = "Researcher un lieu ou un Jolt";
			warning = "Attention";
			confirm = "Confirmer";
			buttonConfirmRegisterName = "Inscription";
			yes = "Oui";
			no = "Non";
			cancel = "Annuler";
			close = "Fermer";
			signUp = "Inscription";
			tryAgain = "Essayez à nouveau!";
			loginNotSuccess = "Connectez-vous pas le succès!";
			accountUjoolt = "Compte Ujoolt";
			alert = "Alerter";
			filter = "FILTRES";
			myJolt = "Mes Jolts";
			recent = "Récents";
			favourite = "Favouris";
			subjectMailReportProblem = "Signaler du Ujoolt Android";
			bodyMailReportProblem = "J'ai trouvé un bug de Ujoolt. ";

			confirmDelete = "Etes-vous sûr de vouloir supprimer ce Jolt?";
			confirmUnfavourite = "Souhaitez vous vraiment retirer ce Jolt de vos favoris ?";
			confirmDeleteFavourite = "Souhaitez vous vraiment retirer ce Jolt de vos favoris?";
			confirmLogout = "Etes-vous sûr de vouloir vous déconnecter?";
			confirmTurnOnGPS = "Votre service de localisation de Google est désactivé, activez-le maintenant?";
			confirmTurnOnNetwork = "Votre réseau est désactivé, activez-le maintenant?";
			confirmExit = "Voulez-vous quitter l'application?";
			confirmLoginFacebook = "Vous devez vous connecter pour poster facebook secousse ami. Connectez-vous maintenant?";

			notifyLogin = "Vous n'avez pas encore de compte!";
			notifyNoResult = "Aucun résultat trouvé!";
			notifyNoNetwork = "Pas de réseau trouvé\n Ressayer plus tard s'il vous plaît!";
			notifyAuthenError = "Erreur authen!";
			notifyVideoMoreThan15s = "Vous ne pouvez pas transférer le visuel de plus de 15 sécondes!";
			notifyEmptyText = "Le texte du Jolt ne peut être vide!";
			notifyCheckEmail = "S'il vous plaît consulter votre courrier électronique à l'état actif!";
			notifyNotEnoughInput = "Tous les champs doivent obligatoirement être remplis!";
			notifyExistEmail = "Cette adresse mail existe déjà cliquer sur Oublié pour recevoir votre mot de passe ou inscrivez-vous avec une autre adresse mail!";
			notifyEmailNotValid = "e-mail n'est pas valide!";
			notifyPassNotMatch = "La vérification du mot de passe ne correspond pas!";
			notifyTurnOnGPS = "Vous devez activer le GPS!";
			notifyDeleteSuccessful = "Suppression réussie!";
		} else {
			login = "Login";
			connectFacebook = "             Connect w/ Facebook";
			connectTwitter = "             Connect w/ Twitter";
			register = "Register";
			locationOrJolt = "Location or Jolt";
			location = "Location";
			push = "Push";
			setting = "Setting";
			languageLabel = "French";
			reportAProblem = "Report a problem";
			tutorial = "Tutorial";
			about = "About";
			pullToRejolt = "Pull to rejolt...";
			releaseToRejolt = "Release to rejolt...";
			userName = "User name";
			eMail = "Email";
			password = "Password";
			loading = "Loading...";
			upLoading = "Uploading...";
			waiting = "Waiting...";
			shareWithFacebook = "Share with facebook";
			shareWithTwitter = "Share with twitter";
			logout = "Logout";
			pleaseWait = "Please wait...";
			typeYourTextHere = "Type your text here...";
			searchLocationOrJolt = "Search location or Jolt";
			warning = "Warning";
			confirm = "Confirm";
			buttonConfirmRegisterName = "SignUp";
			yes = "Yes";
			no = "No";
			cancel = "Cancel";
			close = "Close";
			signUp = "Sign Up";
			tryAgain = "Try again!";
			loginNotSuccess = "Login not success!";
			accountUjoolt = "Account Ujoolt";
			alert = "Alert";
			filter = "FILTER";
			myJolt = "My Jolts";
			recent = "Recent";
			favourite = "Favourite";
			subjectMailReportProblem = "Report problem of Ujoolt Android";
			bodyMailReportProblem = "I found a bug of Ujoolt. ";

			confirmDelete = "Are you sure you want to delete this jolt?";
			confirmUnfavourite = "Do you really want to remove this Jolt from your favorites?";
			confirmDeleteFavourite = "Do you really want to remove this Jolt from your favorites?";
			confirmLogout = "Are you sure you want to log out?";
			confirmTurnOnGPS = "Your Google's location service is disabled, enable now?";
			confirmTurnOnNetwork = "Your network is disabled, enable now?";
			confirmExit = "Do you want to quit the application?";
			confirmLoginFacebook = "You must login facebook to post friend jolt. Login now?";

			notifyLogin = "You have not login yet!";
			notifyNoResult = "No results were found!";
			notifyNoNetwork = "Your network is down.\n Please check your network!";
			notifyAuthenError = "Authen error!";
			notifyVideoMoreThan15s = "You can't upload video longer 15 seconds!";
			notifyEmptyText = "Please enter text for the Jolt!";
			notifyNotEnoughInput = "Please note that all fields are required!";
			notifyExistEmail = "This email address already exist press Forget to receive your password or try to sign up with a deferent email!";
			notifyEmailNotValid = "Email is not valid !";
			notifyCheckEmail = "Please check your email to active!";
			notifyPassNotMatch = "Password verification do not match!";
			notifyTurnOnGPS = "You need to turn on GPS!";
			notifyDeleteSuccessful = "Delete successful!";
		}
	}

	public static String getTimeString(boolean language, int hour, int minute, int second) {
		if (hour > 0) {
			if (hour > 1) {
				if (language == FRANCE) {
					return ("Il y a " + hour + " heures");
				} else {
					return (hour + " hours ago");
				}
			} else {
				if (language == FRANCE) {
					return ("Il y a 1 heure");
				} else {
					return ("1 hour ago");
				}
			}
		} else if (minute > 0) {
			if (minute > 1) {
				if (language == FRANCE) {
					return ("Il y a " + minute + " minutes");
				} else {
					return (minute + " minutes ago");
				}
			} else {
				if (language == FRANCE) {
					return ("Il y a 1 minute");
				} else {
					return ("1 minute ago");
				}
			}
		} else {
			if (second < 10) {
				return ("00:00:0" + second);
			} else {
				return ("00:00:" + second);
			}
		}
	}
}
