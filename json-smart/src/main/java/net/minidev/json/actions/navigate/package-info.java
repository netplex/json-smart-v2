/**
 * <b>Navigate user-specified paths in a {@link net.minidev.json.JSONObject} and process them</b>
 * <p>
 * {@link net.minidev.json.actions.navigate.JSONNavigator} only navigates through branches corresponding
 * to user-specified paths. For each path, the navigation starts at the root and moves down the branch.
 * <p>
 * The {@link net.minidev.json.actions.navigate.JSONNavigator} accepts a
 * {@link net.minidev.json.actions.navigate.NavigateAction} and provides callback hooks at each significant
 * step which the {@link net.minidev.json.actions.navigate.NavigateAction} may use to process
 * the nodes.
 * <p>
 * A path to navigate must be specified in the n-gram format - a list of keys from the root down separated by dots:
 * K0[[[[.K1].K2].K3]...]
 * <br>
 * A key to the right of a dot is a direct child of a key to the left of a dot. Keys with a dot in their name are
 * not supported.
 * <p>
 * Sample usage:
 * <pre>
 * NavigateAction navAction = new NavigateAction(){...};
 * JSONNavigator jsonNav = new JSONNavigator(navAction, "foo.bar.path");
 * jsonNav.nav(new JSONObject(...));
 * Object result = navAction.result();
 * </pre>
 *
 * @author adoneitan@gmail.com
 */
package net.minidev.json.actions.navigate;