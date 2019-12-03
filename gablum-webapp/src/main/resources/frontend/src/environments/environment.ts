// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  wsURL: 'http://localhost:8080/api/auctions/ws',
  loginApi: 'http://localhost:8080/api/users/signin',
  registerApi: 'http://localhost:8080/api/users/signup',
  contractUrl: 'http://localhost:8080/api/contracts',
  profileUrl: 'http://localhost:8080/api/users/profile',
  proposalUrl: 'http://localhost:8080/api/proposals/proposals',
<<<<<<< HEAD
  guestProposallistUrl: 'http://localhost:8080/api/proposals/proposals/browse',
   navlinkUrl: 'http://localhost:8080/api/users/menuitems'
=======
  navlinkUrl: 'http://localhost:8080/api/users/menuitems',
  auctionUrl: 'http://localhost:8080/api/auctions/auctions'
>>>>>>> 6e5cca3515def769399ec7a6337b3d4740bc8d03
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
